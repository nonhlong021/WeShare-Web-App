package weshare.controller;

import io.javalin.http.Handler;
import org.javamoney.moneta.function.MonetaryFunctions;
import org.jetbrains.annotations.NotNull;
import weshare.model.Expense;
import weshare.model.PaymentRequest;
import weshare.model.Person;
import weshare.persistence.ExpenseDAO;
import weshare.server.ServiceRegistry;
import weshare.server.WeShareServer;

import javax.money.MonetaryAmount;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static weshare.model.MoneyHelper.ZERO_RANDS;
import static weshare.model.MoneyHelper.amountOf;


public class PaymentSentController {

    public static final Handler viewSent = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person personLoggedIn = WeShareServer.getPersonLoggedIn(context);

        Collection<PaymentRequest> paymentRequests = expensesDAO.findPaymentRequestsSent(personLoggedIn);
        int total = 0;
        for (PaymentRequest paymentRequest: paymentRequests){
            total += paymentRequest.getAmountToPay().getNumber().intValue();
        }
        System.out.println(total);
        MonetaryAmount GTotal = amountOf(total);
        Map<String, Object> viewModel = Map.of("paymentRequestsSent", paymentRequests, "totalUnpaid", GTotal, "paymnets.html", paymentRequests);
        context.render("paymentrequests_sent.html", viewModel);
    };
}
