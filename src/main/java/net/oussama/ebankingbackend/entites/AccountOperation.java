package net.oussama.ebankingbackend.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.oussama.ebankingbackend.enums.OperationType;

import java.util.Date;
@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class AccountOperation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private Date date;
    private double amount;
    private OperationType type;
    @ManyToOne
    private BankAccount account;
}
