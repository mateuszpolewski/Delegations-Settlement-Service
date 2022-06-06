package pl.pracainz.delegacje.service;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pracainz.delegacje.model.Delegation;
import pl.pracainz.delegacje.model.Expense;
import pl.pracainz.delegacje.model.User;


import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.stream.Stream;


@Service
public class PDFGenerator {
    @Autowired
    DelegationService delegationService;
    @Autowired
    UserService userService;
    public void generateDelegationPdfFile(Delegation delegation) throws FileNotFoundException {
        int delegationId = delegation.getId();
        String dest = "C:/delegacje2/delegacje/delegacja-podsumowanie-" + Integer.toString(delegationId) + ".pdf";

        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdfDoc = new PdfDocument(writer);

        // Creating a Document object
        com.itextpdf.layout.Document doc = new Document(pdfDoc);

        User accountant = userService.findUserById(delegation.getAccountantId());
        String accountantText = "Delegacja rozliczona przez: " + accountant.getName() + " " + accountant.getLastName();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        String dataText = "Data rozliczenia delegacji: " + formatter.format(delegation.getSubmissionDate());

        Paragraph emptyParagraph = new Paragraph();
        Paragraph paragraph1 = new Paragraph(accountantText);
        Paragraph paragraph2 = new Paragraph(dataText);

        // Adding paragraphs to document
        doc.add(paragraph1);
        doc.add(paragraph2);
        doc.add(emptyParagraph);
        // Creating a table
        float [] pointColumnWidths = {200F, 200F};
        Table delegationTable = new Table(pointColumnWidths);
        delegationTable = generateDelegationTable(delegationTable, delegation);
        doc.add(delegationTable);
        doc.add(emptyParagraph);

        float [] pointColumnWidthsExpenses = {80F, 120F, 60F, 50F, 50F, 50F};
        Table expensesTable = new Table(pointColumnWidthsExpenses);
        System.out.println(expensesTable);
        expensesTable = generateExpensesTable(expensesTable, delegation,delegation.getExpenses());
        doc.add(expensesTable);
        doc.add(emptyParagraph);

        String summaryText = "Koszt delegacji: " + (delegation.getSummaryCost() + delegation.getAdvancedPayment() + " [PLN]");
        String employeeSummaryText = "Kwota zwrotu dla pracownika: " + (delegation.getSummaryCost() + " [PLN]");

        Paragraph paragraph3 = new Paragraph(summaryText);
        doc.add(paragraph3);

        Paragraph paragraph4 = new Paragraph(employeeSummaryText);
        doc.add(paragraph4);

        doc.close();

    }

    private Table addExpenseRows(Table table, Set<Expense> expenses) {

        for ( Expense expense : expenses ) {
            table.addCell(expense.getName());
            table.addCell(expense.getDescription());
            String categoryName = expense.getCategory().getName();
            if (categoryName.equals("Ryczałt"))
                categoryName = "Ryczalt";
            table.addCell(categoryName);
            table.addCell(Double.toString(expense.getPrice()));
            table.addCell(expense.getCurrency().getName());
            table.addCell(Double.toString(expense.getValueInPln()));
        }
        return table;
    }

    private Table addExpenseTableHeader(Table table) {
        Stream.of("Nazwa", "Opis", "Kategoria", "Wydatek", "Waluta", "Kwota w PLN")
                .forEach(columnTitle -> {
                    Cell c1 = new Cell();// Creating cell 1
                    c1.add(columnTitle);
                    c1.setBackgroundColor(Color.LIGHT_GRAY);
                    table.addCell(columnTitle);
                });
        return table;

    }


    private Cell formattedDelegationCell(String text) {
        Cell c1 = new Cell();// Creating cell 1
        c1.add(text);                             // Adding name to cell 1
        c1.setBorder(Border.NO_BORDER);              // Setting border
        c1.setTextAlignment(TextAlignment.LEFT);
        return c1;
    }
    private Table generateDelegationTable(Table table, Delegation delegation) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strStartDate = dateFormat.format(delegation.getRealStartDateTime());
        String strEndDate = dateFormat.format(delegation.getRealEndDateTime());
        DateFormat dateFormatWithoutHours = new SimpleDateFormat("yyyy-MM-dd");

        String addedAtDate = dateFormatWithoutHours.format(new java.util.Date());

        table.addCell(formattedDelegationCell(delegation.getTitle()));
        table.addCell(formattedDelegationCell(delegation.getDescription()));
        table.addCell(formattedDelegationCell(delegation.getDestinationCountry()));
        table.addCell(formattedDelegationCell(delegation.getDestinationCity()));
        table.addCell(formattedDelegationCell("Od " + strStartDate));
        table.addCell(formattedDelegationCell("Do " + strEndDate));
        table.addCell(formattedDelegationCell("Dodano przez: " + delegation.getUser().getName()
                + " " +delegation.getUser().getLastName()));
        table.addCell(formattedDelegationCell("Data dodania delegacji: " + addedAtDate));

        return table;
    }
    private Table addAdvancePaymentIssuedToTable(Table table, Delegation delegation) {
        table.addCell("Dostarczona zaliczka");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("-" + delegation.getAdvancedPayment());

        return table;
    }

    private Table generateExpensesTable(Table table, Delegation delegation, Set<Expense> expenses) {
        table = addExpenseTableHeader(table);
        table = addExpenseRows(table, expenses);
        table = addAdvancePaymentIssuedToTable(table, delegation);
        return table;
    }
}
