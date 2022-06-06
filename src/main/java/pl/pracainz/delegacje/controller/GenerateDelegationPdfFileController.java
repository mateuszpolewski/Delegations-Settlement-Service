package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.pracainz.delegacje.model.Delegation;
import pl.pracainz.delegacje.service.DelegationService;
import pl.pracainz.delegacje.service.PDFGenerator;

import java.io.FileNotFoundException;

@Controller
public class GenerateDelegationPdfFileController {
    @Autowired
    DelegationService delegationService;
    @Autowired
    PDFGenerator pdfGenerator;

    @RequestMapping(value = "/auth/delegation-pdf/{id}", method = RequestMethod.GET)
    public ModelAndView approveDelegation(@PathVariable int id, RedirectAttributes redirectAttributes) throws FileNotFoundException {
        Delegation delegation = delegationService.getById(id);
        pdfGenerator.generateDelegationPdfFile(delegation);

        redirectAttributes.addFlashAttribute("successMessage", "Wygenerowano plik pdf");
        return new ModelAndView("redirect:/auth/delegation/" + id);
    }
}
