package com.example.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.finalproject.chess.ChessTurn;
import com.example.finalproject.chess.Figure;
import com.example.finalproject.chess.Players;

import java.util.ArrayList;

public class TilesView extends View {
    int darkColor;
    int brightColor;
    int brightColorCounter;
    boolean cells[][] = new boolean[8][8];
    int cellSize;
    int ia;
    public boolean isHelp;
    boolean isOneMode;
    Paint paintDark;Paint paintBright;Paint alertText;Paint paintDarkA;Paint paintBrightA;
    public Figure[][] map;
    public Bitmap bpawn;
    public Bitmap brook;
    public Bitmap bknight;
    public Bitmap bbishop;
    public Bitmap bqueen;
    public Bitmap bking;
    public Bitmap wpawn;
    public Bitmap wrook;
    public Bitmap wknight;
    public Bitmap wbishop;
    public Bitmap wqueen;
    public Bitmap wking;
    public Players player;
    public Chess bfb;
    public int ttjm;
    public Boolean[][] validMap = new Boolean[8][8];
    public int touchedIm;
    public int touchedJm;
    boolean checkWhite;
    boolean checkBlack;
    int winBarMinus=0;
    String endgameString;
    boolean fChoose = false;
    Players chooser = Players.Black;
    ArrayList<ChessTurn> validTurns;
    ArrayList<ChessTurn> turnList = new ArrayList<>();
    boolean blackKingCounter; boolean blackShortRookCounter; boolean whiteKingCounter; boolean whiteShortRookCounter; boolean blackLongRookCounter; boolean whiteLongRookCounter;

    public TilesView(Context context) {
        super(context);
    }

    public TilesView(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);
        turnList.add(new ChessTurn(0,0,0,0));
        bpawn = BitmapFactory.decodeResource(getResources(),R.drawable.black_pawn);
        brook = BitmapFactory.decodeResource(getResources(),R.drawable.black_rook);
        bknight = BitmapFactory.decodeResource(getResources(),R.drawable.black_knight);
        bbishop = BitmapFactory.decodeResource(getResources(),R.drawable.black_bishop);
        bqueen = BitmapFactory.decodeResource(getResources(),R.drawable.black_queen);
        bking = BitmapFactory.decodeResource(getResources(),R.drawable.black_king);
        wpawn = BitmapFactory.decodeResource(getResources(),R.drawable.white_pawn);
        wrook = BitmapFactory.decodeResource(getResources(),R.drawable.white_rook);
        wknight = BitmapFactory.decodeResource(getResources(),R.drawable.white_knight);
        wbishop = BitmapFactory.decodeResource(getResources(),R.drawable.white_bishop);
        wqueen = BitmapFactory.decodeResource(getResources(),R.drawable.white_queen);
        wking = BitmapFactory.decodeResource(getResources(),R.drawable.white_king);

        darkColor = getResources().getColor(R.color.purple_200);
        brightColor = getResources().getColor(R.color.white);
        brightColorCounter = 0;
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                cells[i][j] = (i+j)%2==0;
                if(cells[i][j]){
                    brightColorCounter++;
                }
            }
        }
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                validMap[i][j]=false;
            }
        }
        paintDark = new Paint();
        paintDark.setColor(darkColor);
        paintBright = new Paint();
        paintBright.setColor(brightColor);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        if(brightColorCounter==0 || brightColorCounter == cells.length * cells[0].length) {
        }else {
            ia = 100;
        }
        cellSize = canvas.getWidth() / 8;
        bpawn = Bitmap.createScaledBitmap(bpawn,cellSize-20,cellSize-20,false);
        brook = Bitmap.createScaledBitmap(brook,cellSize-20,cellSize-20,false);
        bknight = Bitmap.createScaledBitmap(bknight,cellSize-20,cellSize-20,false);
        bbishop = Bitmap.createScaledBitmap(bbishop,cellSize-20,cellSize-20,false);
        bqueen = Bitmap.createScaledBitmap(bqueen,cellSize-20,cellSize-20,false);
        bking = Bitmap.createScaledBitmap( bking,cellSize-20,cellSize-20,false);
        wpawn = Bitmap.createScaledBitmap(wpawn,cellSize-20,cellSize-20,false);
        wrook = Bitmap.createScaledBitmap(wrook,cellSize-20,cellSize-20,false);
        wknight = Bitmap.createScaledBitmap(wknight,cellSize-20,cellSize-20,false);
        wbishop = Bitmap.createScaledBitmap(wbishop,cellSize-20,cellSize-20,false);
        wqueen = Bitmap.createScaledBitmap(wqueen,cellSize-20,cellSize-20,false);
        wking = Bitmap.createScaledBitmap(wking,cellSize-20,cellSize-20,false);

        int offset = 5;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (cells[i][j]) {
                    canvas.drawRect(j * cellSize + offset, i * cellSize + offset,
                            (j + 1) * cellSize - offset, (i + 1) * cellSize - offset, paintBright);
                } else {
                    canvas.drawRect(j * cellSize + offset, i * cellSize + offset,
                            (j + 1) * cellSize - offset, (i + 1) * cellSize - offset, paintDark);
                }
            }
        }
        if(!fChoose) {
            if (endgameString == null) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        switch (map[i][j]) {
                            case White_Pawn:
                                canvas.drawBitmap(wpawn, j * cellSize + offset + 5, i * cellSize + offset + 5, new Paint());
                                break;
                            case White_Bishop:
                                canvas.drawBitmap(wbishop, j * cellSize + offset + 5, i * cellSize + offset + 5, new Paint());
                                break;
                            case White_King:
                                canvas.drawBitmap(wking, j * cellSize + offset + 5, i * cellSize + offset + 5, new Paint());
                                if (checkWhite) {
                                    Paint p1 = new Paint();
                                    p1.setColor(getResources().getColor(R.color.active));
                                    canvas.drawCircle(cellSize * j + cellSize / 2, cellSize * i + cellSize / 2, 30, p1);
                                }
                                break;
                            case White_Knight:
                                canvas.drawBitmap(wknight, j * cellSize + offset + 5, i * cellSize + offset + 5, new Paint());
                                break;
                            case White_Queen:
                                canvas.drawBitmap(wqueen, j * cellSize + offset + 5, i * cellSize + offset + 5, new Paint());
                                break;
                            case White_Rook:
                                canvas.drawBitmap(wrook, j * cellSize + offset + 5, i * cellSize + offset + 5, new Paint());
                                break;
                            case Black_Pawn:
                                canvas.drawBitmap(bpawn, j * cellSize + offset + 5, i * cellSize + offset + 5, new Paint());
                                break;
                            case Black_Bishop:
                                canvas.drawBitmap(bbishop, j * cellSize + offset + 5, i * cellSize + offset + 5, new Paint());
                                break;
                            case Black_King:
                                canvas.drawBitmap(bking, j * cellSize + offset + 5, i * cellSize + offset + 5, new Paint());
                                if (checkBlack) {
                                    Paint p1 = new Paint();
                                    p1.setColor(getResources().getColor(R.color.active));
                                    canvas.drawCircle(cellSize * j + cellSize / 2, cellSize * i + cellSize / 2, 30, p1);
                                }
                                break;
                            case Black_Knight:
                                canvas.drawBitmap(bknight, j * cellSize + offset + 5, i * cellSize + offset + 5, new Paint());
                                break;
                            case Black_Queen:
                                canvas.drawBitmap(bqueen, j * cellSize + offset + 5, i * cellSize + offset + 5, new Paint());
                                break;
                            case Black_Rook:
                                canvas.drawBitmap(brook, j * cellSize + offset + 5, i * cellSize + offset + 5, new Paint());
                                break;
                        }
                    }
                }
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (validMap[i][j]) {
                            Paint p1 = new Paint();
                            p1.setColor(getResources().getColor(R.color.purple_200a));
                            canvas.drawCircle(cellSize * j + cellSize / 2, cellSize * i + cellSize / 2, 10, p1);
                        }
                    }
                }
            } else {
                Paint p1 = new Paint();
                p1.setColor(getResources().getColor(R.color.purple_700));
                Paint p2 = new Paint();
                p2.setColor(getResources().getColor(R.color.purple_200a));
                p2.setTextSize(cellSize/2);
                if(winBarMinus<=(cellSize*2+cellSize/2)){
                    canvas.drawRect(cellSize*3+cellSize/2-winBarMinus, cellSize, cellSize * 4+cellSize/2+winBarMinus, cellSize * 3, p1);
                    winBarMinus+=15;
                    invalidate();
                }
                else {
                    canvas.drawRect(cellSize*3+cellSize/2-winBarMinus, cellSize, cellSize * 4+cellSize/2+winBarMinus, cellSize * 3, p1);
                    canvas.drawText(endgameString, cellSize + 2 * cellSize / 3, cellSize + 2 * cellSize / 3, p2);
                    Paint p3 = new Paint();
                    p3.setColor(getResources().getColor(R.color.black));

                    canvas.drawRect(cellSize * 2 + 10, cellSize * 2 + 10, cellSize * 6 - 10, cellSize * 3 - 10, p3);
                    canvas.drawText("В лобби", cellSize * 3, cellSize * 3 - cellSize / 4, p2);
                }
            }
        }
        else{
            if (chooser == Players.White){
                Paint p2 = new Paint(R.color.teal_700);
                canvas.drawBitmap(wknight,cellSize*2+offset+5,cellSize+offset+5,p2);
                canvas.drawBitmap(wbishop,cellSize*3+offset+5,cellSize+offset+5,p2);
                canvas.drawBitmap(wrook,cellSize*4+offset+5,cellSize+offset+5,p2);
                canvas.drawBitmap(wqueen,cellSize*5+offset+5,cellSize+offset+5,p2);
            }
            else if(chooser == Players.Black){
                Paint p2 = new Paint(R.color.teal_700);
                canvas.drawBitmap(bknight,cellSize*2+offset+5,cellSize*6+offset+5,p2);
                canvas.drawBitmap(bbishop,cellSize*3+offset+5,cellSize*6+offset+5,p2);
                canvas.drawBitmap(brook,cellSize*4+offset+5,cellSize*6+offset+5,p2);
                canvas.drawBitmap(bqueen,cellSize*5+offset+5,cellSize*6+offset+5,p2);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(validTurns == null){
            validTurns = Chess.discoverMate(turnList, map, player, blackKingCounter, blackShortRookCounter, whiteKingCounter, whiteShortRookCounter, blackLongRookCounter, whiteLongRookCounter);
        }
        float x = event.getX();
        float y = event.getY();
        if(endgameString==null){
        int jm = ((int)x)/cellSize;
        int im = ((int)y)/cellSize;

        if(fChoose){
            if(player == Players.White && im == 1){
                if(jm==2){
                    map[touchedIm-1][ttjm] = Figure.White_Knight;
                    map[touchedIm][touchedJm] = Figure.Empty;
                }
                else if(jm==3){
                    map[touchedIm-1][ttjm] = Figure.White_Bishop;
                    map[touchedIm][touchedJm] = Figure.Empty;
                }
                else if(jm==4){
                    map[touchedIm-1][ttjm] = Figure.White_Rook;
                    map[touchedIm][touchedJm] = Figure.Empty;
                }
                else if(jm==5){
                    map[touchedIm-1][ttjm] = Figure.White_Queen;
                    map[touchedIm][touchedJm] = Figure.Empty;
                }
                fChoose=false;
                player = Players.Black;
                checkWhite = Chess.discoverCheck(map,Players.White);
                checkBlack = Chess.discoverCheck(map,Players.Black);
                validTurns = Chess.discoverMate(turnList, map, player, blackKingCounter, blackShortRookCounter, whiteKingCounter, whiteShortRookCounter, blackLongRookCounter, whiteLongRookCounter);
                System.out.println(validTurns);
                if(validTurns.size()==0){
                    if(checkBlack || checkWhite){
                        declareWin(player,1);
                    }
                    else{
                        declareWin( player,2);
                    }
                }
                invalidate();
            }
            else if(player == Players.Black && im == 6){
                if(jm==2){
                    map[touchedIm+1][ttjm] = Figure.Black_Knight;
                    map[touchedIm][touchedJm] = Figure.Empty;
                }
                else if(jm==3){
                    map[touchedIm+1][ttjm] = Figure.Black_Bishop;
                    map[touchedIm][touchedJm] = Figure.Empty;
                }
                else if(jm==4){
                    map[touchedIm+1][ttjm] = Figure.Black_Rook;
                    map[touchedIm][touchedJm] = Figure.Empty;
                }
                else if(jm==5){
                    map[touchedIm+1][ttjm] = Figure.Black_Queen;
                    map[touchedIm][touchedJm] = Figure.Empty;
                }
                fChoose=false;
                player=Players.White;
                checkWhite = Chess.discoverCheck(map,Players.White);
                checkBlack = Chess.discoverCheck(map,Players.Black);
                validTurns = Chess.discoverMate(turnList, map, player, blackKingCounter, blackShortRookCounter, whiteKingCounter, whiteShortRookCounter, blackLongRookCounter, whiteLongRookCounter);
                System.out.println(validTurns);
                if(validTurns.size()==0){
                    if(checkBlack || checkWhite){
                        declareWin(player,1);
                    }
                    else{
                        declareWin( player,2);
                    }
                }
                invalidate();
            }
            turnList.add(new ChessTurn(touchedJm, touchedIm, ttjm, touchedIm+1));
        }
        else{
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                if(validMap[im][jm]) {
                    if (map[touchedIm][touchedJm] == Figure.White_Pawn && im==touchedIm-1 && (jm == touchedJm-1 || jm == touchedJm+1) && map[im][jm]==Figure.Empty){

                            map[im][jm] = map[touchedIm][touchedJm];
                            map[touchedIm][touchedJm] = Figure.Empty;
                            map[touchedIm][jm] = Figure.Empty;
                    }
                    else if (map[touchedIm][touchedJm] == Figure.Black_King || map[touchedIm][touchedJm] == Figure.White_King) {
                        map[im][jm] = map[touchedIm][touchedJm];
                        map[touchedIm][touchedJm] = Figure.Empty;
                        if (jm == touchedJm - 2) {
                            map[im][jm + 1] = map[im][0];
                            map[im][0] = Figure.Empty;
                        } else if (jm == touchedJm + 2) {
                            map[im][jm - 1] = map[im][7];
                            map[im][7] = Figure.Empty;
                        }
                        if (player == Players.White) {
                            whiteKingCounter = true;
                        } else {
                            blackKingCounter = true;
                        }
                    } else if (map[touchedIm][touchedJm] == Figure.Black_Rook || map[touchedIm][touchedJm] == Figure.White_Rook) {
                        map[im][jm] = map[touchedIm][touchedJm];
                        map[touchedIm][touchedJm] = Figure.Empty;
                        if (player == Players.White) {
                            if (touchedJm == 0) {
                                whiteLongRookCounter = true;
                            } else if (touchedJm == 7) {
                                whiteShortRookCounter = true;
                            }

                        } else {
                            if (touchedJm == 0) {
                                blackLongRookCounter = true;
                            } else if (touchedJm == 7) {
                                blackShortRookCounter = true;
                            }
                        }
                    } else if (map[touchedIm][touchedJm] == Figure.Black_Pawn && im == 7 || map[touchedIm][touchedJm] == Figure.White_Pawn && im == 0) {
                        figureChoose(player);
                        ttjm = jm;
                    } else {
                        map[im][jm] = map[touchedIm][touchedJm];
                        map[touchedIm][touchedJm] = Figure.Empty;
                    }
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            validMap[i][j] = false;
                        }
                    }
                    turnList.add(new ChessTurn(touchedJm, touchedIm, jm, im));
                    if(!fChoose){
                    if(player==Players.White){player = Players.Black;}
                    else{player=Players.White;}}
                    checkWhite = Chess.discoverCheck(map,Players.White);
                    checkBlack = Chess.discoverCheck(map,Players.Black);
                    validTurns = Chess.discoverMate(turnList, map, player, blackKingCounter, blackShortRookCounter, whiteKingCounter, whiteShortRookCounter, blackLongRookCounter, whiteLongRookCounter);
                    System.out.println(validTurns);
                    if(validTurns.size()==0){
                        if(checkBlack || checkWhite){
                            declareWin(player,1);
                        }
                        else{
                            declareWin( player,2);
                        }
                    }
                }
                else if(map[im][jm] !=Figure.Empty) {
                    for(int i=0;i<8;i++){
                        for(int j=0;j<8;j++){
                            validMap[i][j]=false;
                        }
                    }
                    for(int i=0;i<validTurns.size();i++){
                        ChessTurn turn = validTurns.get(i);
                        if(turn.x_start == jm && turn.y_start == im){
                            validMap[turn.y_finish][turn.x_finish] = true;

                        }
                    }
                    touchedIm=im;
                    touchedJm=jm;
                }
                else{
                    for(int i=0;i<8;i++){
                        for(int j=0;j<8;j++){
                            validMap[i][j]=false;
                        }
                    }
                }
            }
        }
        }
        else{
            if(x >= cellSize*2+10 && x<=cellSize*6-10 && y>=cellSize*2+10 && y<=cellSize*3-10){
                bfb.finish();
            }
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    private void figureChoose(Players player){
        fChoose = true;
        chooser = player;
        invalidate();
    }

    public void declareWin(Players player, int cause){
        if(player==Players.White){
            if(cause==1){
                endgameString = "Мат. Победа чёрных.";
            }
            else if(cause == 3){
                endgameString = "Белые сдались.";
            }
            else{
                endgameString = "Ничья.";
            }
        }
        else{
            if(cause==1){
                endgameString = "Мат. Победа белых.";
            }
            else if(cause == 3){
                endgameString = "Чёрные сдались.";
            }
            else{
                endgameString = "Ничья.";
            }
        }
    }

//    private void changeColor(int i, int j){
//        if (cells[i][j]) {
//            cells[i][j] = false;
//            brightColorCounter--;
//        } else {
//            cells[i][j] = true;
//            brightColorCounter++;
//        }
//    }
//    private ArrayList<Integer> computeSolution(){
//        int[] linesCount = new int[cells.length];
//        int[] rowsCount = new int[cells[0].length];
//        int[][] cellCount = new int[cells.length][cells[0].length];
//        for(int i=0;i<cells.length; i++){
//            for(int j=0;j<cells[0].length;j++){
//                if(cells[i][j]) {
//                    linesCount[i]++;
//                    rowsCount[j]++;
//                    cellCount[i][j]=1;
//                }
//            }
//        }
//        ArrayList<Integer> result = new ArrayList<>();
//        for(int i=0;i<cells.length; i++){
//            for(int j=0;j<cells[0].length;j++) {
//                if((linesCount[i] + rowsCount[j] - cellCount[i][j])%2==1){
//                    result.add(i*cells[0].length + j);
//                }
//            }
//        }
//        return result;
//    }
}
