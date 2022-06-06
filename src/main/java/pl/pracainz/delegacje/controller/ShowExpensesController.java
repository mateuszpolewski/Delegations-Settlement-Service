package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.pracainz.delegacje.model.Delegation;
import pl.pracainz.delegacje.model.Expense;
import pl.pracainz.delegacje.model.Photo;
import pl.pracainz.delegacje.repository.DelegationRepository;
import pl.pracainz.delegacje.service.DelegationService;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;


@Controller
public class ShowExpensesController {

    @Autowired
    DelegationService delegationService;
    @Autowired
    DelegationRepository delegationRepository;
    @Autowired
    MobileApplicationController mobileApplicationController;

    @RequestMapping(value="/auth/delegation/{id}/expenses", method = RequestMethod.GET)
    public ModelAndView showExpenses(ModelAndView modelAndView, @PathVariable("id") int delegationId) throws IOException {

        List<Expense> mainList = new ArrayList<Expense>();
        mainList.addAll(delegationService.getById(delegationId).getExpenses());
        Collections.sort(mainList, (d1, d2) -> {
            return d2.getId() - d1.getId();
        });

        try {
            expensePhotosToFile(mainList);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        modelAndView.setViewName("/auth/expenses");
        modelAndView.addObject("delegationId", delegationId);
        modelAndView.addObject("delegationForPayment", new Delegation());
        modelAndView.addObject("delegation", delegationService.getById(delegationId));
        modelAndView.addObject("expenses",mainList );
        return modelAndView;
    }
    @RequestMapping(value="/auth/delegation/{id}/expenses", method = RequestMethod.POST)
    public ModelAndView showExpensesAndChangeAdvPayment(ModelAndView modelAndView, @PathVariable("id") int delegationId,
                                                        @Valid Delegation delegation, BindingResult bindingResult){

        if(!bindingResult.hasErrors()) {
            Delegation delegationToSave = delegationService.getById(delegationId);
            //delegationToSave.setAdvancePaymentIssued(Double.parseDouble(delegation.getPaymentTemp()));
            delegationRepository.save(delegationToSave);
        }
        List<Expense> mainList = new ArrayList<Expense>();
        mainList.addAll(delegationService.getById(delegationId).getExpenses());
        Collections.sort(mainList, (d1, d2) -> {
            return d2.getId() - d1.getId();
        });
        modelAndView.setViewName("/auth/expenses");
        modelAndView.addObject("delegationId", delegationId);
        modelAndView.addObject("delegationForPayment", new Delegation());
        modelAndView.addObject("delegation", delegationService.getById(delegationId));
        modelAndView.addObject("expenses", mainList);

        return modelAndView;
    }

    public void expensePhotosToFile(List<Expense> expensesList) throws IOException {
        String absolutePath = new File("").getAbsolutePath();
        for (Expense expense : expensesList) {
            for (Photo photo : expense.getPhotos()) {
                fileUpload(photo, absolutePath);
            }
        }
    }
    public void fileUpload(Photo photo, String absolutePath) throws IOException {
        String filename = photo.getName();
        byte[] bytes = mobileApplicationController.decompressBytes(photo.getPicByte());
        Path path = Paths.get(absolutePath + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "images" + File.separator + filename);
        Files.write(path, bytes);
    }
}

