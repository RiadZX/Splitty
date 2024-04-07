package client.utils;

import commons.Participant;

public record DebtResolveResult(Participant from, Participant to, Double amount) {

}
