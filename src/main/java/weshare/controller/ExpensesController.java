package weshare.controller;

import io.javalin.http.Handler;
import org.javamoney.moneta.function.MonetaryFunctions;
import org.jetbrains.annotations.NotNull;
import weshare.model.Expense;
import weshare.model.Person;
import weshare.persistence.ExpenseDAO;
import weshare.server.Routes;
import weshare.server.ServiceRegistry;
import weshare.server.WeShareServer;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static weshare.model.MoneyHelper.ZERO_RANDS;
import static weshare.model.MoneyHelper.amountOf;

public class ExpensesController {

    public static final Handler view = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person personLoggedIn = WeShareServer.getPersonLoggedIn(context);

        Collection<Expense> expenses = expensesDAO.findExpensesForPerson(personLoggedIn)
                .stream().filter(e -> !e.isFullyPaidByOthers()).collect(Collectors.toList());
        int grandTotal = 0;
        for(Expense expense: expenses){
            grandTotal += expense.getAmount().getNumber().intValue();
        }
        MonetaryAmount GTotal = amountOf(grandTotal);
        System.out.println(GTotal);
        Map<String, Object> viewModel = Map.of("expenses", expenses, "grandTot", GTotal);
        context.render("expenses.html", viewModel);
    };



    public static final Handler AddNewExpense = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person personLoggedIn = WeShareServer.getPersonLoggedIn(context);
        String description = context.formParam("description");
        String [] dateList = context.formParam("date").split("/");
        LocalDate date = LocalDate.of(Integer.parseInt(dateList[2]),
                Integer.parseInt(dateList[1]),
                Integer.parseInt(dateList[0]));

        MonetaryAmount amount = amountOf(Integer.parseInt(context.formParam("amount")));
        Expense newExpense = new Expense(personLoggedIn, description, amount, date);
        expensesDAO.save(newExpense);
        context.redirect(Routes.EXPENSES);
    };
}
