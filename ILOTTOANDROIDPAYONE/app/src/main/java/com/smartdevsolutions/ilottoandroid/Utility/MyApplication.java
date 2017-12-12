package com.smartdevsolutions.ilottoandroid.Utility;

import android.app.Application;



import java.util.List;

/**
 * Created by T360-INNOVATIVZ on 22/05/2017.
 */

public class MyApplication extends Application {
    private TicketInput CurrentTicket;
    private Ticket  CheckTicketCurrentTicket;
    private BetInput CurrentBet;
    private DailyGame SelectedGame;
    private Customer CurrentCustomer;
    private List<DailyGame> FetchedGames;
    private List<Customer> CashiersInShop;








    public DailyGame getSelectedGame() {
        return SelectedGame;
    }

    public void setSelectedGame(DailyGame selectedGame) {
        SelectedGame = selectedGame;
    }

    public TicketInput getCurrentTicket() {
        return CurrentTicket;
    }

    public void setCurrentTicket(TicketInput currentTicket) {
        CurrentTicket = currentTicket;
    }

    public Ticket getCheckTicketCurrentTicket() {
        return CheckTicketCurrentTicket;
    }

    public void setCheckTicketCurrentTicket(Ticket checkTicketCurrentTicket) {
        CheckTicketCurrentTicket = checkTicketCurrentTicket;
    }

    public List<DailyGame> getFetchedGames() {
        return FetchedGames;
    }

    public void setFetchedGames(List<DailyGame> fetchedGames) {
        FetchedGames = fetchedGames;
    }

    public Customer getCurrentCustomer() {
        return CurrentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        CurrentCustomer = currentCustomer;
    }

    public BetInput getCurrentBet() {
        return CurrentBet;
    }

    public void setCurrentBet(BetInput currentBet) {
        CurrentBet = currentBet;
    }

    public  void setCashiersInShop(List<Customer> Cashiers)
    {
        CashiersInShop = Cashiers;
    }

    public  List<Customer> getCashiersInShop() {
        return CashiersInShop;
    }


}
