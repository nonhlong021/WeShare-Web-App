package weshare.controller;

import io.javalin.http.Handler;
import weshare.model.*;
import weshare.persistence.ExpenseDAO;
import weshare.server.ServiceRegistry;
import weshare.server.WeShareServer;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static weshare.model.MoneyHelper.amountOf;

public class PaymentReceivedController {

    public static final Handler viewReceived = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person personLoggedIn = WeShareServer.getPersonLoggedIn(context);

        Collection<PaymentRequest> paymentRequests = expensesDAO.findPaymentRequestsReceived(personLoggedIn);
        int total = findTotal(paymentRequests);
        MonetaryAmount GTotal = amountOf(total);
        Map<String, Object> viewModel = Map.of("paymentRequestsReceived", paymentRequests, "totalUnpaid", GTotal);
        context.render("paymentrequests_received.html", viewModel);
    };

    public static final Handler payReceived = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person personLoggedIn = WeShareServer.getPersonLoggedIn(context);

        UUID id = UUID.fromString(context.formParam("id"));
        Collection<PaymentRequest> requests = expensesDAO.findPaymentRequestsReceived(personLoggedIn);

        Payment payment = null;
        for (PaymentRequest request : requests) {
            if(request.getId().equals(id)){
                payment = request.getExpense().payPaymentRequest(id, personLoggedIn, DateHelper.TODAY);
            }
        }
        expensesDAO.save(payment.getExpenseForPersonPaying());

        Collection<PaymentRequest> paymentRequests = expensesDAO.findPaymentRequestsReceived(personLoggedIn);
        int total = findTotal(paymentRequests);

        MonetaryAmount GTotal = amountOf(total);
        Map<String, Object> viewModel = Map.of("paymentRequestsReceived", paymentRequests, "totalUnpaid", GTotal);
        context.render("paymentrequests_received.html", viewModel);
    };

    public static int findTotal(Collection<PaymentRequest> paymentRequests){
        int total = 0;
        for (PaymentRequest paymentRequest: paymentRequests){
            if(!paymentRequest.isPaid()){
                total += paymentRequest.getAmountToPay().getNumber().intValue();
            }
        }
        return total;
    }
}
