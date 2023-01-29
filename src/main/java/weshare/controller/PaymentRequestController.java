package weshare.controller;

import io.javalin.http.Handler;
import weshare.model.Expense;
import weshare.model.PaymentRequest;
import weshare.model.Person;
import weshare.persistence.ExpenseDAO;
import weshare.server.ServiceRegistry;
import weshare.server.WeShareServer;

import javax.money.MonetaryAmount;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static weshare.model.MoneyHelper.amountOf;

public class PaymentRequestController {

    public static final Handler paymentParameter = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person personLoggedIn = WeShareServer.getPersonLoggedIn(context);
        UUID expenseId = UUID.fromString(context.queryParam("expenseId"));

        Expense expense = expensesDAO.get(expenseId).get();
        Map<String, Object> viewModel = Map.of("expense", expense);

        context.render("paymentrequest.html", viewModel);
    };
}

