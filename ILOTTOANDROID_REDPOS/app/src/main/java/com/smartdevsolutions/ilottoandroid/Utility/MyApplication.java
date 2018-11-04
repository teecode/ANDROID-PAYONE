package com.smartdevsolutions.ilottoandroid.Utility;

import android.app.Application;

import com.smartdevsolutions.ilottoandroid.ApiResource.AddBetSlipResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.AddTicketResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.DailyGameResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.DeviceResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.TicketResource;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;

import android_serialport_api.SerialPort;
import hdx.HdxUtil;

/**
 * Created by T360-INNOVATIVZ on 22/05/2017.
 */

public class MyApplication extends Application {
    private AddTicketResource CurrentTicket;
    private TicketResource CheckTicketCurrentTicket;
    private AddBetSlipResource CurrentBet;
    private DailyGameResource SelectedGame;
    private DeviceResource CurrentTerminal;

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    private String Token;
    private List<DailyGameResource> FetchedGames;
    private List<Customer> CashiersInShop;

    private SerialPort mSerialPort = null;

    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
			/* Read serial port parameters */
            //SharedPreferences sp = getSharedPreferences("android_serialport_api.sample_preferences", MODE_PRIVATE);
            String path = HdxUtil.GetPrinterPort();//dev/ttyS3
            int baudrate = 115200;//Integer.decode(sp.getString("BAUDRATE", "-1"));

			/* Open the serial port */
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
        }
        return mSerialPort;
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }


    public DailyGameResource getSelectedGame() {
        return SelectedGame;
    }

    public void setSelectedGame(DailyGameResource selectedGame) {
        SelectedGame = selectedGame;
    }

    public AddTicketResource getCurrentTicket() {
        return CurrentTicket;
    }

    public void setCurrentTicket(AddTicketResource currentTicket) {
        CurrentTicket = currentTicket;
    }

    public TicketResource getCheckTicketCurrentTicket() {
        return CheckTicketCurrentTicket;
    }

    public void setCheckTicketCurrentTicket(TicketResource checkTicketCurrentTicket) {
        CheckTicketCurrentTicket = checkTicketCurrentTicket;
    }

    public List<DailyGameResource> getFetchedGames() {
        return FetchedGames;
    }

    public void setFetchedGames(List<DailyGameResource> fetchedGames) {
        FetchedGames = fetchedGames;
    }

    public DeviceResource getCurrentTerminal() {
        return CurrentTerminal;
    }

    public void setCurrentTerminal(DeviceResource currentTerminal) {
        CurrentTerminal = currentTerminal;
    }

    public AddBetSlipResource getCurrentBet() {
        return CurrentBet;
    }

    public void setCurrentBet(AddBetSlipResource currentBet) {
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
