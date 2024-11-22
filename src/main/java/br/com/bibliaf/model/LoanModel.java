package br.com.bibliaf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "loans")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoanModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private BookModel book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;

    @PrePersist
    protected void onCreate() {
        if (this.loanDate == null) {
            this.loanDate = LocalDate.now();
            this.returnDate = loanDate.plusWeeks(1);
        }
        this.status = LoanStatus.PENDING;
    }

    public LoanStatus getStatus() {
        checkOverdueStatus();
        return status;
    }

    private void checkOverdueStatus() {
        if (this.returnDate.isBefore(LocalDate.now()) && this.status != LoanStatus.RETURNED) {
            this.status = LoanStatus.OVERDUE;
        }
        else if (!this.returnDate.isBefore(LocalDate.now()) && this.status != LoanStatus.RETURNED) {
            this.status = LoanStatus.PENDING;
        }
    }

    public enum LoanStatus {
        PENDING,
        RETURNED,
        OVERDUE
    }
}
