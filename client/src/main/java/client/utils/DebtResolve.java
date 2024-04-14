package client.utils;

import commons.Debt;
import commons.Event;
import commons.Expense;
import commons.Participant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebtResolve {

    private static HashMap<Participant, Amounts> gather(Event event) {
        // gathers the total amounts everyone spent and received

        HashMap<Participant, Amounts> amounts = new HashMap<>();

        List<Expense> expenses = event.getExpenses();

        for (Expense e : expenses) {
            List<Debt> debts = e.getDebts();
            Participant person = e.getPaidBy();
            for (Debt d : debts) {
                if (2d.isPaid()) continue;
                amounts.merge(d.getParticipant(),
                        new Amounts(0., d.getAmount()),
                        (start, end) -> new Amounts(
                                start.giving() + end.giving(),
                                start.getting() + end.getting()
                        )
                );

                amounts.merge(person,
                        new Amounts(d.getAmount(), 0.),
                        (start, end) -> new Amounts(
                                start.giving() + end.giving(),
                                start.getting() + end.getting()
                        )
                );
            }
        }

        return amounts;
    }

    public static List<DebtResolveResult> resolve(Event event) {
        HashMap<Participant, Amounts> amounts = gather(event);



        HashMap<Participant, Double> netAmounts = new HashMap<>();
        double maxAmount = Double.NEGATIVE_INFINITY;

        for (Map.Entry<Participant, Amounts> a : amounts.entrySet()) {
            // calculate net spent
            Double net = a.getValue().net();
            netAmounts.put(a.getKey(), net);
            if (net > maxAmount) {
                maxAmount = net;
            }
        }

        List<Tuple<Participant, Double>> payments = new ArrayList<>();

        for (Map.Entry<Participant, Double> a : netAmounts.entrySet()) {
            payments.add(new Tuple<>(a.getKey(), a.getValue()));
        }

        payments.sort((a, b) -> a.b().compareTo(b.b()));

        System.out.println(payments);

        List<DebtResolveResult> res = new ArrayList<>();

        for (int i = 0; i < (payments.size() - 1); i++) {
            Tuple<Participant, Double> amount = payments.get(i+1);
            payments.set(i+1, new Tuple<>(
                    amount.a(),
                    amount.b() + payments.get(i).b()
            ));
            res.add(
                    new DebtResolveResult(
                            payments.get(i).a(),
                            payments.get(i+1).a(),
                            -payments.get(i).b()
                    )
            );
        }

        //netAmounts.forEach((key, value) -> res.add(new DebtResolveResult(key, event.getExpenses().getFirst().getPaidBy(), value)));

        return res;
    }
}

record Amounts(Double giving, Double getting) {
    public Amounts add(Amounts one, Amounts two) {
        return new Amounts(
                one.giving + two.giving,
                one.getting + two.getting
        );
    }

    public Double net() {
        return this.giving - this.getting;
    }
}

record Tuple<A, B>(A a, B b) {}
