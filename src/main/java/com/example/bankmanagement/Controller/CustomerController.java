package com.example.bankmanagement.Controller;

import com.example.bankmanagement.ApiResponse.ApiResponse;
import com.example.bankmanagement.Model.Customer;
import com.example.bankmanagement.dto.AmountRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    ArrayList<Customer> customers = new ArrayList<>();

    //BASIC CRUD ENDPOINTS
    @GetMapping("/get")
    public ArrayList<Customer> getCustomers(){
        return customers;
    }

    @PostMapping("/add")
    public ApiResponse addCustomer(@RequestBody Customer customer){
        for(Customer c: customers){
            if(c.getId().equalsIgnoreCase(customer.getId()))
                return new ApiResponse("Customer with this ID already exists");
        }
        customers.add(customer);
        return new ApiResponse("Customer added successfully");
    }

    @PutMapping("/update/{id}")
    public ApiResponse updateCustomer(@PathVariable String id, @RequestBody Customer customer){
        for(int i = 0; i < customers.size(); i++){
            if(customers.get(i).getId().equalsIgnoreCase(id)){
                customers.set(i, customer);
                return new ApiResponse("Customer updated successfully");
            }
        }
        return new ApiResponse("Customer not found");
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteCustomer(@PathVariable String id){
        for(int i = 0; i < customers.size(); i++){
            if(customers.get(i).getId().equalsIgnoreCase(id)){
                customers.remove(i);
                return new ApiResponse("Customer deleted successfully");
            }
        }
        return new ApiResponse("Customer not found");
    }


    //EXTRA ENDPOINTS
    @PutMapping("/deposit/{id}")
    public ApiResponse deposit(@PathVariable String id, @RequestBody AmountRequest request){
        if(request.getAmount() <= 0) return new ApiResponse("Please enter positive amount");

        for (Customer customer : customers) {
            if (customer.getId().equalsIgnoreCase(id)) {
                customer.setBalance(customer.getBalance() + request.getAmount());
                return new ApiResponse("Deposit done successfully. New balance: " + customer.getBalance());
            }
        }
        return new ApiResponse("Customer not found");
    }

    @PutMapping("/withdraw/{id}")
    public ApiResponse withdraw(@PathVariable String id, @RequestBody AmountRequest request){
        if(request.getAmount() <= 0) return new ApiResponse("Please enter positive amount");

        for (Customer customer : customers) {
            if (customer.getId().equalsIgnoreCase(id)) {
                if(request.getAmount() > customer.getBalance()) return new ApiResponse("Insufficient balance");
                customer.setBalance(customer.getBalance() - request.getAmount());
                return new ApiResponse("Withdraw done successfully. New balance: " + customer.getBalance());
            }
        }
        return new ApiResponse("Customer not found");
    }
}
