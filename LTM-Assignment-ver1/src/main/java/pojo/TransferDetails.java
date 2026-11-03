package pojo;

public class TransferDetails {
    private Long fromAccountNumber;
    private Long toAccountNumber;
    private Double transferAmount;

    public Long getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(Long fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public Long getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(Long toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public Double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Double transferAmount) {
        this.transferAmount = transferAmount;
    }

    public static TransferDetails getTransferPayload(Long fromAccountNumber, Long toAccountNumber, Double transferAmount) {
        TransferDetails transferDetails = new TransferDetails();
        transferDetails.fromAccountNumber = fromAccountNumber;
        transferDetails.toAccountNumber = toAccountNumber;
        transferDetails.transferAmount = transferAmount;
        return transferDetails;
    }

    @Override
    public String toString() {
        return "{fromAccountNumber=" + fromAccountNumber + ", toAccountNumber=" + toAccountNumber + ", transferAmount=" + transferAmount + "}";
    }
}

