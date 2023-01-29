package weshare.controller;

import io.javalin.http.Handler;
import weshare.model.DateHelper;
import weshare.model.Expense;
import weshare.model.Payment;
import weshare.model.Person;
import weshare.persistence.ExpenseDAO;
import weshare.persistence.PersonDAO;
import weshare.server.Routes;
import weshare.server.ServiceRegistry;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import static weshare.model.DateHelper.TODAY;
import static weshare.model.MoneyHelper.amountOf;

public class PaymentRequestActionController {

    public static Handler paymentAction = context -> {
        UUID expenseId = UUID.fromString(
                context.formParamAsClass("expenseId", String.class)
                        .check(Objects::nonNull, "Missing Expense ID").get()
        );
//        UUID requestId = UUID.fromString(
//                context.formParamAsClass("paymentRequestId", String.class)
//                        .check(Objects::nonNull, "Missing Payment Request ID").get()
//        );
        String email = context.formParamAsClass("email", String.class)
                .check(Objects::nonNull, "Email is required")
                .get();

        int amount1 = context.formParamAsClass("amount", int.class)
                .check(Objects::nonNull, "Amount is required")
                .get();

        MonetaryAmount amount = amountOf(amount1);

        String date = context.formParamAsClass("due_date", String.class)
                .check(Objects::nonNull, "Date is required")
                .get();

        LocalDate localDate = LocalDate.parse(date);

        PersonDAO personDAO = ServiceRegistry.lookup(PersonDAO.class);
        ExpenseDAO expenseDAO = ServiceRegistry.lookup(ExpenseDAO.class);

        Person personWhoShouldPayBack = personDAO.findPersonByEmail(email).orElseThrow();
        Expense expense = expenseDAO.get(expenseId).orElseThrow();

        expense.requestPayment(personWhoShouldPayBack,amount,localDate);

//        Payment payment = expense.payPaymentRequest(requestId, personWhoShouldPayBack, TODAY);
//
//        Expense newExpense = new Expense(
//                payment.getPersonPaying(), expense.getDescription(), payment.getAmountPaid(), payment.getPaymentDate()
//        );

//        expenseDAO.save(newExpense);
//        System.out.println("check me out");
        context.redirect("/paymentrequest?expenseId="+expense.getId());

    };
}
