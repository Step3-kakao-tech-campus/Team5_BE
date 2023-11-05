package com.kakao.sunsuwedding.payment;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TossPaymentResponse {

    public record TosspayDTO(
             String version,
             String paymentKey,
             String type,
             String orderId,
             String orderName,
             String mId,
             String currency,
             String method,
             Number totalAmount,
             Number balanceAmount,
             String status,
             String requestedAt,
             String approvedAt,
             Boolean useEscrow,
             String lastTransactionKey,
             Number suppliedAmount,
             Number vat,
             Boolean cultureExpense,
             Number taxFreeAmount,
             Integer taxExemptionAmount,
             List<Cancel> cancels,
             Boolean isPartialCancelable,
             Card card,
             VirtualAccount virtualAccount,
             String secret,
             MobilePhone mobilePhone,
             GiftCertificate giftCertificate,
             Transfer transfer,
             Receipt receipt,
             Checkout checkout,
             EasyPay easyPay,
             String country,
             Failure failure,
             CashReceipt cashReceipt,
             List<CashReceipts> cashReceipts ,
             Discount discount
    ){
    }

    private record Discount(
            Integer amount
    ) {
    }

    private record CashReceipts(
         String receiptKey,
         String orderId,
         String type,
         String issueNumber,
         String receiptUrl,
         String businessNumber,
         String transactionType,
         Integer amount,
         Integer taxFreeAmount,
         String issueStatus ,
         ReceiptFailure failure,
         String customerIdentityNumber,
         String requestedAt
    ){
    }

    private record ReceiptFailure(
         Integer code,
         String message
    ){
    }

    private record CashReceipt(
            String type,
            String receiptKey ,
            String issueNumber ,
            String receiptUrl ,
            Number amount ,
            Number taxFreeAmount
    ) {
    }

    private record Failure (
            String code,
            String message
    ){
    }

    private record EasyPay(
         String provide,
         Number amount,
         Number discountAmount
    ){}

    private record Checkout(
         String url
    ) {
    }

    private record Receipt(
         String url
    ) {
    }

    private record Transfer(
        String bankCode,
        String settlementStatus
    ) {
    }

    private record GiftCertificate(
        String approveNo,
        String settlementStatus
    ) {
    }

    private record MobilePhone(
            String customerMobilePhone,
            String settlementStatus,
            String receiptUrl
    ) {
    }

    private record VirtualAccount (
             String accountType,
             String accountNumber,
             String bankCode,
             String customerName,
             String dueDate ,
             String refundStatus,
             Boolean expired ,
             String settlementStatus ,
             RefundReceiveAccount refundReceiveAccount
    ){

    }

    private record RefundReceiveAccount(
             String bankCode,
             String accountNumber,
             String holderName
    ) {

    }
    private record Cancel(
             Number cancelAmount,
             String cancelReason,
             Number taxFreeAmount,
             Integer taxExemptionAmount,
             Number refundableAmount,
             Number easyPayDiscountAmount,
             String canceledAt ,
             String transactionKey ,
             String receiptKey
    ) {

    }

    private record Card (
         Number amount,
         String issuerCode,
         String acquirerCode,
         String number,
         Integer installmentPlanMonths,
         String approveNo,
         Boolean useCardPoint,
         String cardType,
         String ownerType,
         String acquireStatus,
         Boolean isInterestFree,
         String interestPayer
    ){
    }
}
