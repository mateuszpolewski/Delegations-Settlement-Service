package pl.pracainz.delegacje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pracainz.delegacje.model.User;
import pl.pracainz.delegacje.model.UserMobile;
import pl.pracainz.delegacje.repository.DelegationRepository;
import pl.pracainz.delegacje.repository.ExpenseRepository;
import pl.pracainz.delegacje.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.pracainz.delegacje.model.*;
import pl.pracainz.delegacje.repository.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@RestController
@RequestMapping("/mobile")
public class MobileApplicationController {
    @Autowired
    DelegationRepository delegationRepository;
    @Autowired
    ExpenseRepository expenseRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CurrencyRepository currencyRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    PhotoRepository photoRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("setPassword/{id}/{password}")
    public String changeTemporaryPassword(@PathVariable int id,
                                          @PathVariable("password") String password){
        User user = userRepository.findById(id);
        if(user != null && user.getPassword().isTemporary()){
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            user.getPassword().setPasswordHash(bCryptPasswordEncoder.encode(password));
            user.getPassword().setTemporary(false);
            userRepository.save(user);
            return "succ";
        }
        return "failure";
    }

    @GetMapping("login/{email}/{password}")
    public UserMobile loginToMobileApp(@PathVariable String email,
                                       @PathVariable("password") String password){
        UserMobile userMobile = new UserMobile();
        User user = userRepository.findByEmail(email);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(null != user && user.getActive()
                && bCryptPasswordEncoder.matches
                (password,user.getPassword().getPasswordHash()))
        {
            userMobile.setId(user.getId());
            userMobile.setName(user.getName());
            userMobile.setLastName(user.getLastName());
            userMobile.setEmail(user.getEmail());
            userMobile.setPhoneNumber(user.getPhoneNumber());
            userMobile.setTemporaryPassword(user.getPassword().isTemporary());
            userMobile.setPosition(user.getPosition());
            userMobile.setComment(user.getComment());
            userMobile.setMessage("success");
        } else{
            userMobile.setMessage("failure");
        }
        return userMobile;
    }

    @GetMapping("delegations/{userId}")
    public List<DelegationMobile> getUserDelegations(@PathVariable int userId) {
        List<Delegation> allUserDelegations = delegationRepository.findDelegationsByUser_Id(userId);
        List<DelegationMobile> allUserDelegationsMobile = new ArrayList<>();

        for(Delegation delegation : allUserDelegations) {
            DelegationMobile delegationMobile = new DelegationMobile();

            delegationMobile.setId(delegation.getId());
            delegationMobile.setTitle(delegation.getTitle());
            delegationMobile.setDescription(delegation.getDescription());
            delegationMobile.setStartDate(delegation.getStartDate());
            delegationMobile.setEndDate(delegation.getEndDate());
            delegationMobile.setDestinationCountry(delegation.getDestinationCountry());
            delegationMobile.setDestinationCity(delegation.getDestinationCity());
            delegationMobile.setSummaryCost(delegation.getSummaryCost());
            delegationMobile.setAdvancedPayment(delegation.getAdvancedPayment());
             delegationMobile.setAccountantId(delegation.getAccountantId());
            delegationMobile.setRealStartDateTime(delegation.getRealStartDateTime());
            delegationMobile.setRealEndDateTime(delegation.getRealEndDateTime());
            delegationMobile.setSubmissionDate(delegation.getSubmissionDate());
            delegationMobile.setUser_id(delegation.getUser().getId());
// TODO missing value delegation_status_id in database
            delegationMobile.setDelegation_status_id(delegation.getDelegationStatus().getId());

            allUserDelegationsMobile.add(delegationMobile);
        }
        return allUserDelegationsMobile;
    }

    @GetMapping("expenses/{delegationId}")
    public List<ExpenseMobile> getDelegationExpenses(@PathVariable int delegationId) {
        List<Expense> allDelegationExpenses = expenseRepository.findExpensesByDelegation_Id(delegationId);
        List<ExpenseMobile> allDelegationExpensesMobile = new ArrayList<>();

        for(Expense expense : allDelegationExpenses) {
            ExpenseMobile expenseMobile = new ExpenseMobile();

            expenseMobile.setId(expense.getId());
            expenseMobile.setTitle(expense.getName());
            expenseMobile.setDescription(expense.getDescription());
            expenseMobile.setPrice(expense.getPrice());

            allDelegationExpensesMobile.add(expenseMobile);
        }
        return allDelegationExpensesMobile;
    }

    @GetMapping("expenses/currencyList")
    public List<String> getCurrencyList(){
        List<Currency> currencyTableObjects = currencyRepository.findAll();
        List<String> currencyNameList = new ArrayList<>();
        for (int i=0; i <= currencyTableObjects.size()-1; i++){
            currencyNameList.add(currencyTableObjects.get(i).getName());
        }

        return currencyNameList;
    }

    @GetMapping("expenses/categoryList")
    public List<String> getCategoryList(){
        List<Category> categoryTableObjects = categoryRepository.findAll();
        List<String> categoryNameList = new ArrayList<>();
        for (int i=0; i <= categoryTableObjects.size()-1; i++){
            categoryNameList.add(categoryTableObjects.get(i).getName());
        }

        return categoryNameList;
    }

    @PostMapping("expenses/add")
    public String setNewExpense(@RequestBody ExpenseMobile expenseMobile){
        Expense expenseToAdd = new Expense();
        expenseToAdd.setName(expenseMobile.getTitle());
        expenseToAdd.setDescription(expenseMobile.getDescription());
        expenseToAdd.setPrice(expenseMobile.getPrice());
        expenseToAdd.setValueInPln(expenseMobile.getValueInPln());

        Delegation delegation = delegationRepository.findById(expenseMobile.getDelegation_id());
        expenseToAdd.setDelegation(delegation);

        Category category = categoryRepository.findById(expenseMobile.getCategory_id());
        expenseToAdd.setCategory(category);

        Currency currency = currencyRepository.findById(expenseMobile.getCurrency_id());
        expenseToAdd.setCurrency(currency);

        expenseRepository.save(expenseToAdd);

        return "success!";

    }



    @PostMapping("upload-photo")
    public String uploadExpensePhoto(@RequestPart("imageFile") MultipartFile file,
                                     @RequestPart("expenseId") int expenseId) throws IOException {
        Photo photo = new Photo(file.getOriginalFilename(), file.getContentType(), compressBytes(file.getBytes()));
        Expense expense = expenseRepository.findById(expenseId);
        photo.setExpense(expense);
        photoRepository.save(photo);
        return "OK";
    }

// compress the image bytes before storing it in the database
    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        deflater.finish();

        byte[] buffer = new byte[1024]; //1024

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

        // uncompress the image bytes before returning it to the application
    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024]; //1024
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }
}
