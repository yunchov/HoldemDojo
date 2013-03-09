package com.nedogeek.holdem.dealer;

import com.nedogeek.holdem.GameSettings;
import com.nedogeek.holdem.gamingStuff.Player;
import com.nedogeek.holdem.gamingStuff.PlayersList;

import java.util.Arrays;

/**
 * User: Konstantin Demishev
 * Date: 22.11.12
 * Time: 1:35
 */
public class EndGameManager {
    private final Dealer dealer;
    private final PlayersList playersList;


    public EndGameManager(Dealer dealer, PlayersList playersList) {
        this.dealer = dealer;
        this.playersList = playersList;
    }

    public void endGame() {
        rewardWinners();
        dealer.setInitialGameRound();
    }

    private void rewardWinners() {
        Object[] winCandidates = playersList.toArray();
        Arrays.sort(winCandidates);

        for (int i = winCandidates.length - 1; i != -1; i--) {
            Player winPlayer = (Player) winCandidates[i];
            giveMoneyToWinner(winPlayer);
        }
        checkZeroBalance();
    }

    private void checkZeroBalance() {
        for (Player player : playersList) {
            if (player.getBalance() == 0) {
                player.setBalance(GameSettings.COINS_AT_START);
                System.out.println("Giving chips to " + player);
            }
        }
    }

    private void giveMoneyToWinner(Player winner) {

        int prize = 0;
        for (Player player : playersList) {
            if (player != winner) {
                prize += getChipsFromPlayer(player, winner.getBet());
            }
        }
        prize += winner.getBet();
        winner.setBet(0);
        winner.setBalance(winner.getBalance() + prize);
        if (prize != 0) {
            dealer.setPlayerWin(winner);
        }
    }

    private int getChipsFromPlayer(Player player, int chipsCount) {
        int playerBet = player.getBet();
        int chipsFromPlayer = (playerBet < chipsCount) ?
                playerBet : chipsCount;

        player.setBet(playerBet -= chipsFromPlayer);

        return chipsFromPlayer;
    }
}
