package com.smartdevsolutions.ilottoandroid.Utility;

import com.smartdevsolutions.ilottoandroid.ApiResource.AddBetSlipResource;
import com.smartdevsolutions.ilottoandroid.ApiResource.BetTypeResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by T360-INNOVATIVZ on 23/05/2017.
 */

public class LottoCalculations {

    private List<String> possiblewinningstring;

    public List<String> getPossiblewinningstring() {
        return possiblewinningstring;
    }

    public void setPossiblewinningstring(List<String> possiblewinningstring) {
        this.possiblewinningstring = possiblewinningstring;
    }

    public int factorial(int n)
    {
        if(n == 0 || n == 1)
            return  1;
        else
            return  (n * factorial(n-1));
    }

    public  int permutation(int n, int r)
    {
        return  factorial(n)/factorial(n-r);
    }

    public  int combination(int n, int r)
    {

        return  (factorial(n)/(factorial(n-r)*factorial(r)));
    }
    public BetTypeResource getCurrentBetBetType(AddBetSlipResource Bet, List<BetTypeResource> bettypes){
        List<BetTypeResource> ALlbetTypes = bettypes;
        for (BetTypeResource btp:ALlbetTypes) {
            if(btp.getBetTypeID()== Bet.getNap()){
                return  btp;
            }
        }
        return  null;
    }

    public AddBetSlipResource DoCalculation(AddBetSlipResource Bet, List<BetTypeResource> bettypes) {
        BetTypeResource currentBetType = getCurrentBetBetType(Bet, bettypes);
        if (!currentBetType.equals("AG") && !currentBetType.getNap().equals("AGS")) {
            int bet1count = Bet.getBet1().size();
            int r = currentBetType.getMinimumNumberOfBalls();
            int lines = combination(bet1count, r);
            double stperline = Bet.getStakeperline();
            Bet.setLines(lines);
            if(stperline >0) {
                //Bet.setAmount(lines * stperline);
                //Bet.setPossiblewinnings(calculatepossiblewinning(Bet));
            }
            return Bet;
        } else if (currentBetType.getNap().equals( "AG"))
        {
            int bet1count = Bet.getBet1().size();
            int r = currentBetType.getMinimumNumberOfBalls();
            int lines = againstRange(Bet.getBet1(), Bet.getBet2());
            double stperline = Bet.getStakeperline();
            Bet.setLines(lines);
            if(stperline >0) {
               // Bet.setAmount(lines * stperline);
               // Bet.setPossiblewinnings(calculatepossiblewinning(Bet));
            }
            return Bet;
        }
        else if(currentBetType.getNap().equals("AGS"))
        {
            int bet1count = Bet.getBet1().size();
            int r = currentBetType.getMinimumNumberOfBalls();
            int lines = againstSingles(Bet.getBet1(), Bet.getBet2());
            double stperline = Bet.getStakeperline();
            Bet.setLines(lines);
            if(stperline >0) {
               // Bet.setAmount(lines * stperline);
               // Bet.setPossiblewinnings(calculatepossiblewinning(Bet));
            }
            return Bet;
        }
        return  Bet;
    }


    public double calculatepossiblewinning( BetInput BetSlip) {
        int minBall = BetSlip.getMin_no_of_balls();
        int maxBall = BetSlip.getMaxball();
        possiblewinningstring = new ArrayList<>();
        int loopend = 0 ;
        if(!BetSlip.getNap().equals("AG") && !BetSlip.getNap().equals("AGS")) {
            try {
                loopend = (BetSlip.getBet1().size() > 5) ? 5 : BetSlip.getBet1().size();

                int loop = 0;
                for (int i = minBall; i <= loopend; i++) {
                    int wonlines = factorial(i) / (factorial(i - minBall) * factorial(minBall));
                    double wonamt = getPossibleWinnngs(wonlines, BetSlip.getStakeperline(), BetSlip.getWinfactor());
                    possiblewinningstring.add((i + " BALLS = ₦" + wonamt));
                    loop++;
                    if (i == loopend) {
                        return wonamt;

                    }
                }
            }
            catch (Exception ex)
            {
                return 0;
            }
        }
        else if(BetSlip.getNap().equals("AG"))
        {
            try {
                int ballnumb = BetSlip.getBet1().size() + Integer.parseInt(BetSlip.getBet2().get(1)) - Integer.parseInt(BetSlip.getBet2().get(0));
                loopend = (ballnumb > 5) ? 5 : ballnumb;

                int loop = 0;
                for (int i = minBall; i <= loopend; i++) {
                    int wonlines = factorial(i) / (factorial(i - minBall) * factorial(minBall));
                    double wonamt = getPossibleWinnngs(wonlines, BetSlip.getStakeperline(), BetSlip.getWinfactor());
                    possiblewinningstring.add((i + " BALLS = ₦" + wonamt));
                    loop++;
                    if (i == loopend) {
                        return wonamt;

                    }
                }
            }
            catch (Exception ex)
            {
                return 0;
            }

        }

        else if(BetSlip.getNap().equals("AGS"))
        {
            try {
                int ballnumb = BetSlip.getBet1().size() + BetSlip.getBet2().size();
                loopend = (ballnumb > 5) ? 5 : ballnumb;

                int loop = 0;
                for (int i = minBall; i <= loopend; i++) {
                    int wonlines = factorial(i) / (factorial(i - minBall) * factorial(minBall));
                    double wonamt = getPossibleWinnngs(wonlines, BetSlip.getStakeperline(), BetSlip.getWinfactor());
                    possiblewinningstring.add((i + " BALLS = ₦" + wonamt));
                    loop++;
                    if (i == loopend) {
                        return wonamt;

                    }
                }
            }
            catch (Exception ex)
            {
                return 0;
            }

        }

        return getPossibleWinnngs(loopend, BetSlip.getStakeperline(), BetSlip.getWinfactor());
    }

    public double getPossibleWinnngs(double lines,double amount, double factor) {
        if (lines == 0 || amount == 0) return 0;
        return lines * amount * factor;
    }


    int againstRange(List<Integer>  bet1, List<Integer> bet2)
    {
        try {
            int collisioncount = 0;
            int countbet1 = bet1.size();

            int start = Integer.parseInt("" + bet2.get(0));
            int end = Integer.parseInt("" + bet2.get(1));
            int i;
            for (i = 0; i < bet1.size(); i++) {
                int x = Integer.parseInt("" + bet1.get(i));
                collisioncount += checkCollisonAgainstRange(x, bet2);


            }

            return (countbet1 * (end - start + 1) - collisioncount);
        }
        catch (Exception ex)
        {
            return  0;
        }

    }

    int checkCollisonAgainstRange(int x, List<Integer> bet2)
    {

        int ret = 0;
        int start = Integer.parseInt(""+bet2.get(0));
        int end = Integer.parseInt(""+bet2.get(1));
        int input = x;
        while (start <= end)
        {
            if (input == start)
            {
                return 1;
            }
            start++;
        }
        return ret;

    }

    int againstSingles(List<Integer>  bet1, List<Integer> bet2)
    {
        try {
            int collisioncount = 0;
            int countbet1 = bet1.size();
            int countbet2 = bet2.size();


            int i;
            for (i = 0; i < bet1.size(); i++) {
                int x = Integer.parseInt("" + bet1.get(i));
                collisioncount += checkCollisonAgainstSingles(x, bet2);


            }

            return (countbet1 * countbet2 - collisioncount);
        }
        catch (Exception ex)
        {
          return  0;
        }

    }

    int checkCollisonAgainstSingles(int x, List<Integer> bet2)
    {
        int ret = 0;
        ;
        int input = x;
        int bet2size = bet2.size();

        int i;
        for (i = 0; i < bet2size; i++)
        {
            int elementin2 = bet2.get(i);
            if (input == elementin2)
            {
                return 1;
            }

        }
        return ret;

    }

}
