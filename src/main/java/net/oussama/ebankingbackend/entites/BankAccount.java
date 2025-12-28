package net.oussama.ebankingbackend.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.oussama.ebankingbackend.enums.AccountStatus;

import java.util.Date;
import java.util.List;
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length =4)
@Data @NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
    @Id
    private String id;
    private double balance;
    private Date date;
    private AccountStatus status;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "account")
    private List<AccountOperation> operations;
}
