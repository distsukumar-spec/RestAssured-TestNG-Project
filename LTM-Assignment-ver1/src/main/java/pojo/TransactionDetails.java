package pojo;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class TransactionDetails {
    private Long accountNumber;
    private String txDateTime;
    private String txType;
    private Double txAmount;

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTxDateTime() {
        return txDateTime;
    }

    public void setTxDateTime() {
        this.txDateTime = DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .withZone(java.time.ZoneOffset.UTC)
                .format(Instant.now());;
    }

    public String getTxType() {
        return txType;
    }

    public void setTxType(String txType) {
        this.txType = txType;
    }

    public Double getTxAmount() {
        return txAmount;
    }

    public void setTxAmount(Double txAmount) {
        this.txAmount = txAmount;
    }

    @Override
    public String toString() {
        return "{accountNumber=" + accountNumber + ", txDateTime=" + txDateTime + ", txType=" + txType +
                ", txAmount=" + txAmount + "}";
    }
}

