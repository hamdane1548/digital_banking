package net.oussama.ebankingbackend.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name;
    private String LastName;
    private String Email;
    @OneToMany(mappedBy = "customer")
    private List<BankAccount> BankAccounts;
}
