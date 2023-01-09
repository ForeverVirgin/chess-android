package com.example.finalproject;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.chess.ChessTurn;
import com.example.finalproject.chess.Figure;
import com.example.finalproject.chess.Players;

import java.util.ArrayList;

public class Chess extends AppCompatActivity {
    TilesView field;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);
        Intent intent = getIntent();
        Figure[][] map = generateMap(8);
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                System.out.print(map[i][j]+" ");
            }
            System.out.println();
        }
        field = findViewById(R.id.field);
        field.map=map;
        Players player = Players.White;
        field.player = player;
        field.bfb = this;
    }

    private Figure[][] generateMap(int n){
        Figure[][] map = new Figure[n][n];
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(i==n-2){map[i][j]=Figure.White_Pawn;}
                else if(i==1){map[i][j]=Figure.Black_Pawn;}
                else if(i==0){
                    if(j==0 || j==n-1){map[i][j] = Figure.Black_Rook;}
                    else if(j==1 || j==n-2){map[i][j] = Figure.Black_Knight;}
                    else if(j==2 || j==n-3){map[i][j] = Figure.Black_Bishop;}
                    else if(j==3){map[i][j] = Figure.Black_Queen;}
                    else if(j==n-4){map[i][j] = Figure.Black_King;}
                }
                else if(i==n-1){
                    if(j==0 || j==n-1){map[i][j] = Figure.White_Rook;}
                    else if(j==1 || j==n-2){map[i][j] = Figure.White_Knight;}
                    else if(j==2 || j==n-3){map[i][j] = Figure.White_Bishop;}
                    else if(j==3){map[i][j] = Figure.White_Queen;}
                    else if(j==n-4){map[i][j] = Figure.White_King;}
                }
                else{map[i][j] = Figure.Empty;}
            }
        }
        return map;
    }
    public void surrender(View v){
        field.declareWin(field.player, 3);
        field.invalidate();
    }
    public static Boolean[][] findValidTurns(ArrayList<ChessTurn> turnList, int im, int jm, Figure[][] map, Players player, boolean blackKingCounter, boolean blackShortRookCounter, boolean whiteKingCounter, boolean whiteShortRookCounter, boolean blackLongRookCounter, boolean whiteLongRookCounter){
        Boolean[][] validMap = new Boolean[8][8];
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                validMap[i][j]=false;
            }
        }

        switch (map[im][jm]){
            case White_Bishop: if(player==Players.White){
                rightBottomValidate(map,validMap,im,jm,player);
                leftBottomValidate(map,validMap,im,jm,player);
                rightTopValidate(map,validMap,im,jm,player);
                leftTopValidate(map,validMap,im,jm,player);
            }
                break;
            case White_King:
                if(player==Players.White){
                    if(im+1<8){
                        if(jm-1>=0){
                            validMap[im+1][jm-1]=validateTile(map,im+1,jm-1,player) && !isUnderAttack(map, im+1, jm-1, player);
                        }
                        validMap[im+1][jm]=validateTile(map,im+1,jm,player) && !isUnderAttack(map, im+1, jm, player);
                        if(jm+1<8){
                            validMap[im+1][jm+1]=validateTile(map,im+1,jm+1,player) && !isUnderAttack(map, im+1, jm+1, player);
                        }
                    }
                    if(im-1>=0){
                        if(jm-1>=0){
                            validMap[im-1][jm-1]=validateTile(map,im-1,jm-1,player) && !isUnderAttack(map, im-1, jm-1, player);
                        }
                        validMap[im-1][jm]=validateTile(map,im-1,jm,player) && !isUnderAttack(map, im-1, jm, player);
                        if(jm+1<8){
                            validMap[im-1][jm+1]=validateTile(map,im-1,jm+1,player) && !isUnderAttack(map, im-1, jm+1, player);
                        }
                    }
                    if(jm-1>=0){
                        validMap[im][jm-1]=validateTile(map,im,jm-1,player) && !isUnderAttack(map, im, jm-1, player);
                    }
                    if(jm+1<8){
                        validMap[im][jm+1]=validateTile(map,im,jm+1,player) && !isUnderAttack(map, im, jm+1, player);
                    }
                    validateShortRook(player,map,validMap, whiteKingCounter, whiteShortRookCounter);
                    validateLongRook(player,map,validMap, whiteKingCounter, whiteLongRookCounter);
                }
                break;
            case White_Knight: if(player==Players.White){knightTurnValidate(map,validMap,im,jm,player);} break;
            case White_Pawn:
                if(player==Players.White){
                    ChessTurn t = turnList.get(turnList.size()-1);
                    if(im-1>=0){if(map[im-1][jm]==Figure.Empty){validMap[im-1][jm]=true;
                        if(im-2>=0){
                        validMap[im-2][jm]=im==6 && map[im-2][jm]==Figure.Empty;}}}
                    if(jm+1<8){
                        validMap[im-1][jm+1]= map[im-1][jm+1]==Figure.Black_Bishop || map[im-1][jm+1]==Figure.Black_Pawn ||
                                map[im-1][jm+1]==Figure.Black_Queen || map[im-1][jm+1]==Figure.Black_Knight ||
                                map[im-1][jm+1]==Figure.Black_Rook || (t.x_finish==jm+1 && t.x_start==jm+1 && t.y_finish==3 && t.y_start==1 && map[3][jm+1]==Figure.Black_Pawn && im==3);
                    }
                    if(jm-1>=0){

                        validMap[im-1][jm-1] = map[im-1][jm-1]==Figure.Black_Bishop || map[im-1][jm-1]==Figure.Black_Pawn ||
                                map[im-1][jm-1]==Figure.Black_Queen || map[im-1][jm-1]==Figure.Black_Knight ||
                                map[im-1][jm-1]==Figure.Black_Rook || (t.x_finish==jm-1 && t.x_start==jm-1 && t.y_finish==3 && t.y_start==1 && map[3][jm-1]==Figure.Black_Pawn && im==3);
                    }
                } break;
            case White_Queen: if(player==Players.White) {
                topValidate(map,validMap,im,jm,player);
                bottomValidate(map,validMap,im,jm,player);
                rightValidate(map,validMap,im,jm,player);
                leftValidate(map,validMap,im,jm,player);
                rightBottomValidate(map,validMap,im,jm,player);
                leftBottomValidate(map,validMap,im,jm,player);
                rightTopValidate(map,validMap,im,jm,player);
                leftTopValidate(map,validMap,im,jm,player);
            }
                break;
            case White_Rook: if(player==Players.White) {
                topValidate(map, validMap, im, jm, player);
                bottomValidate(map, validMap, im, jm, player);
                rightValidate(map, validMap, im, jm, player);
                leftValidate(map, validMap, im, jm, player);
            }
                break;
            case Black_Bishop:  if(player==Players.Black){
                rightBottomValidate(map,validMap,im,jm,player);
                leftBottomValidate(map,validMap,im,jm,player);
                rightTopValidate(map,validMap,im,jm,player);
                leftTopValidate(map,validMap,im,jm,player);
            }
                break;
            case Black_King: if(player==Players.Black){
                if(im+1<8){
                    if(jm-1>=0){
                        validMap[im+1][jm-1]=validateTile(map,im+1,jm-1,player) && !isUnderAttack(map, im+1, jm-1, player);
                    }
                    validMap[im+1][jm]=validateTile(map,im+1,jm,player) && !isUnderAttack(map, im+1, jm, player);
                    if(jm+1<8){
                        validMap[im+1][jm+1]=validateTile(map,im+1,jm+1,player) && !isUnderAttack(map, im+1, jm+1, player);
                    }
                }
                if(im-1>=0){
                    if(jm-1>=0){
                        validMap[im-1][jm-1]=validateTile(map,im-1,jm-1,player) && !isUnderAttack(map, im-1, jm-1, player);
                    }
                    validMap[im-1][jm]=validateTile(map,im-1,jm,player) && !isUnderAttack(map, im-1, jm, player);
                    if(jm+1<8){
                        validMap[im-1][jm+1]=validateTile(map,im-1,jm+1,player) && !isUnderAttack(map, im-1, jm+1, player);
                    }
                }
                if(jm-1>=0){
                    validMap[im][jm-1]=validateTile(map,im,jm-1,player) && !isUnderAttack(map, im, jm-1, player);
                }
                if(jm+1>=0){
                    validMap[im][jm+1]=validateTile(map,im,jm+1,player) && !isUnderAttack(map, im, jm+1, player);
                }
                validateShortRook(player,map,validMap,blackKingCounter, blackShortRookCounter);
                validateLongRook(player,map,validMap, blackKingCounter, blackLongRookCounter);
            }
                break;
            case Black_Knight: if(player==Players.Black){knightTurnValidate(map,validMap,im,jm,player);} break;
            case Black_Pawn:
                if(player==Players.Black){
                    if(im+1<8){
                        if(map[im+1][jm]==Figure.Empty){validMap[im+1][jm]=true;
                            if(im+2<8){validMap[im+2][jm]=im==1 && map[im+2][jm]==Figure.Empty;}}
                    if(jm+1<8){
                        validMap[im+1][jm+1]=map[im+1][jm+1]==Figure.White_Bishop || map[im+1][jm+1]==Figure.White_Pawn || map[im+1][jm+1]==Figure.White_Queen || map[im+1][jm+1]==Figure.White_Knight || map[im+1][jm+1]==Figure.White_Rook;}
                    if(jm-1>=0){
                        validMap[im+1][jm-1]=map[im+1][jm-1]==Figure.White_Bishop || map[im+1][jm-1]==Figure.White_Pawn ||
                                map[im+1][jm-1]==Figure.White_Queen || map[im+1][jm-1]==Figure.White_Knight ||
                                map[im+1][jm-1]==Figure.White_Rook;}}
                } break;
            case Black_Queen: if(player==Players.Black) {
                topValidate(map,validMap,im,jm,player);
                bottomValidate(map,validMap,im,jm,player);
                rightValidate(map,validMap,im,jm,player);
                leftValidate(map,validMap,im,jm,player);
                rightBottomValidate(map,validMap,im,jm,player);
                leftBottomValidate(map,validMap,im,jm,player);
                rightTopValidate(map,validMap,im,jm,player);
                leftTopValidate(map,validMap,im,jm,player);
            }
                break;
            case Black_Rook: if(player==Players.Black) {
                topValidate(map, validMap, im, jm, player);
                bottomValidate(map, validMap, im, jm, player);
                rightValidate(map, validMap, im, jm, player);
                leftValidate(map, validMap, im, jm, player);
            }
                break;
        }
        return validMap;
    }
    private static void validateShortRook(Players player, Figure[][] map, Boolean[][] validMap, boolean kingCounter, boolean shortRookCounter){
        if(player==Players.Black){
            if(!kingCounter && !shortRookCounter){
                validMap[0][6]=!isUnderAttack(map,0,5,player) && !isUnderAttack(map,0,4,player) && !isUnderAttack(map,0,6,player) && validateTile(map,0,5,player) && validateTile(map,0,6,player);
            }
        }
        else{
            if(!kingCounter && !shortRookCounter){
                validMap[7][6] = !isUnderAttack(map,7,5,player) && !isUnderAttack(map,7,4,player) && !isUnderAttack(map,7,6,player)&& validateTile(map,7,5,player) && validateTile(map,7,6,player);
            }
        }
    }
    private static void validateLongRook(Players player, Figure[][] map, Boolean[][] validMap, boolean kingCounter, boolean longRookCounter){
        if(player==Players.Black){
            if(!kingCounter && !longRookCounter){
                validMap[0][2]=!isUnderAttack(map,0,3,player) && !isUnderAttack(map,0,4,player) && !isUnderAttack(map,0,2,player) && validateTile(map,0,3,player) && validateTile(map,0,2,player);
            }
        }
        else{
            if(!kingCounter && !longRookCounter){
                validMap[0][2]=!isUnderAttack(map,7,3,player) && !isUnderAttack(map,7,4,player) && !isUnderAttack(map,7,2,player) && validateTile(map,7,3,player) && validateTile(map,7,2,player);
            }
        }
    }
    private static boolean isUnderAttack(Figure[][] map, int im, int jm, Players player){
        if(player==Players.White){
            if(im-1>=1){
                if(jm-1>=0){if(map[im-1][jm-1]==Figure.Black_Pawn){return true;}}
                if(jm+1<8){if(map[im-1][jm+1]==Figure.Black_Pawn){return true;}}
            }
            if(im-2>=0){
                if(jm-1>=0){if(map[im-2][jm-1]==Figure.Black_Knight){return true;}}
                if(jm+1<8){if(map[im-2][jm+1]==Figure.Black_Knight){return true;}}
            }
            if(im+2<8){
                if(jm-1>=0){if(map[im+2][jm-1]==Figure.Black_Knight){return true;}}
                if(jm+1<8){if(map[im+2][jm+1]==Figure.Black_Knight){return true;}}
            }
            if(jm+2<8){
                if(im-1>=0){if(map[im-1][jm+2]==Figure.Black_Knight){return true;}}
                if(im+1<8){if(map[im+1][jm+2]==Figure.Black_Knight){return true;}}
            }
            if(jm-2>=0){
                if(im-1>=0){if(map[im-1][jm-2]==Figure.Black_Knight){return true;}}
                if(im+1<8){if(map[im+1][jm-2]==Figure.Black_Knight){return true;}}
            }
            if(im-1>=0){
                if(jm-1>=0){
                    if(map[im-1][jm-1]==Figure.Black_King){return true;}
                }
                if(jm+1<8){
                    if(map[im-1][jm+1]==Figure.Black_King){return true;}
                }
                if(map[im-1][jm]==Figure.Black_King){return true;}
            }
            if(im+1<8){
                if(jm-1>=0){
                    if(map[im+1][jm-1]==Figure.Black_King){return true;}
                }
                if(jm+1<8){
                    if(map[im+1][jm+1]==Figure.Black_King){return true;}
                }
                if(map[im+1][jm]==Figure.Black_King){return true;}
            }
            if(jm+1<8){
                if(map[im][jm+1]==Figure.Black_King){return true;}
            }
            if(jm-1>=0){
                if(map[im][jm-1]==Figure.Black_King){return true;}
            }
            if(checkTopWhite(map,im,jm)){return true;};
            if(checkBottomWhite(map,im,jm)){return true;}
            if(checkLeftWhite(map,im,jm)){return true;}
            if(checkRightWhite(map,im,jm)){return true;}
            if(checkRightTopWhite(map,im,jm)){return true;}
            if(checkRightBottomWhite(map,im,jm)){return true;}
            if(checkLeftBottomWhite(map,im,jm)){return true;}
            if(checkLeftTopWhite(map,im,jm)){return true;}
        }
        else {
            if(im+1<7){
                if(jm-1>=0){
                    if(map[im+1][jm-1]==Figure.White_Pawn){return true;}
                }
                if(jm+1<8){
                    if(map[im+1][jm+1]==Figure.White_Pawn){return true;}
                }
            }
            if(im-2>=0){
                if(jm-1>=0) {
                    if(map[im-2][jm-1]==Figure.White_Knight){return true;}}
                if(jm+1<8)  {if(map[im-2][jm+1]==Figure.White_Knight){return true;}}
            }
            if(im+2<8){
                if(jm-1>=0){if(map[im+2][jm-1]==Figure.White_Knight){return true;}}
                if(jm+1<8){if(map[im+2][jm+1]==Figure.White_Knight){return true;}}
            }
            if(jm+2<8){
                if(im-1>=0){if(map[im-1][jm+2]==Figure.White_Knight){return true;}}
                if(im+1<8){if(map[im+1][jm+2]==Figure.White_Knight){return true;}}
            }
            if(jm-2>=0){
                if(im-1>=0){if(map[im-1][jm-2]==Figure.White_Knight){return true;}}
                if(im+1<8){if(map[im+1][jm-2]==Figure.White_Knight){return true;}}
            }
            if(im-1>=0){
                if(jm-1>=0){
                    if(map[im-1][jm-1]==Figure.White_King){return true;}
                }
                if(jm+1<8){
                    if(map[im-1][jm+1]==Figure.White_King){return true;}
                }
                if(map[im-1][jm]==Figure.White_King){return true;}
            }
            if(im+1<8){
                if(jm-1>=0){
                    if(map[im+1][jm-1]==Figure.White_King){return true;}
                }
                if(jm+1<8){
                    if(map[im+1][jm+1]==Figure.White_King){return true;}
                }
                if(map[im+1][jm]==Figure.White_King){return true;}
            }
            if(jm+1<8){
                if(map[im][jm+1]==Figure.White_King){return true;}
            }
            if(jm-1>=0){
                if(map[im][jm-1]==Figure.White_King){return true;}
            }
            if(checkTopBlack(map,im,jm)){return true;};
            if(checkBottomBlack(map,im,jm)){return true;}
            if(checkLeftBlack(map,im,jm)){return true;}
            if(checkRightBlack(map,im,jm)){return true;}
            if(checkRightTopBlack(map,im,jm)){return true;}
            if(checkRightBottomBlack(map,im,jm)){return true;}
            if(checkLeftBottomBlack(map,im,jm)){return true;}
            if(checkLeftTopBlack(map,im,jm)){return true;}
        }
        return false;
    }
    private static boolean checkTopWhite(Figure[][] map, int im, int jm){
        if(im-1>=0){
            if(map[im-1][jm]==Figure.Empty){return checkTopWhite(map,im-1,jm);}
            else if(map[im-1][jm]==Figure.Black_Rook || map[im-1][jm]==Figure.Black_Queen){return true;}
            else return false;
        }
        else return false;
    }
    private static boolean checkBottomWhite(Figure[][] map, int im, int jm){
        if(im+1<8){
            if(map[im+1][jm]==Figure.Empty){return checkBottomWhite(map,im+1,jm);}
            else if(map[im+1][jm]==Figure.Black_Rook || map[im+1][jm]==Figure.Black_Queen){return true;}
            else return false;
        }
        else return false;
    }
    private static boolean checkRightWhite(Figure[][] map, int im, int jm){
        if(jm+1<8){
            if(map[im][jm+1]==Figure.Empty){return checkRightWhite(map,im,jm+1);}
            else if(map[im][jm+1]==Figure.Black_Rook || map[im][jm+1]==Figure.Black_Queen){return true;}
            else return false;
        }
        else return false;
    }
    private static boolean checkLeftWhite(Figure[][] map, int im, int jm){
        if(jm-1>=0){
            if(map[im][jm-1]==Figure.Empty){return checkLeftWhite(map,im,jm-1);}
            else if(map[im][jm-1]==Figure.Black_Rook || map[im][jm-1]==Figure.Black_Queen){return true;}
            else return false;
        }
        else return false;
    }
    private static boolean checkLeftTopWhite(Figure[][] map, int im, int jm){
        if(im-1>=0 && jm-1>=0){
            if(map[im-1][jm-1]==Figure.Empty){return checkLeftTopWhite(map,im-1,jm-1);}
            else if(map[im-1][jm-1]==Figure.Black_Bishop || map[im-1][jm-1]==Figure.Black_Queen){return true;}
            else return false;
        }
        else return false;
    }
    private static boolean checkRightTopWhite(Figure[][] map, int im, int jm){
        if(im-1>=0 && jm+1<8){
            if(map[im-1][jm+1]==Figure.Empty){return checkRightTopWhite(map,im-1,jm+1);}
            else if(map[im-1][jm+1]==Figure.Black_Bishop || map[im-1][jm+1]==Figure.Black_Queen){return true;}
            else return false;
        }
        else return false;
    }
    private static boolean checkLeftBottomWhite(Figure[][] map, int im, int jm){
        if(im+1<8 && jm-1>=0){
            if(map[im+1][jm-1]==Figure.Empty){return checkLeftBottomWhite(map,im+1,jm-1);}
            else if(map[im+1][jm-1]==Figure.Black_Bishop || map[im+1][jm-1]==Figure.Black_Queen){return true;}
            else return false;
        }
        else return false;
    }
    private static boolean checkRightBottomWhite(Figure[][] map, int im, int jm){
        if(im+1<8 && jm+1<8){
            if(map[im+1][jm+1]==Figure.Empty){return checkRightBottomWhite(map,im+1,jm+1);}
            else if(map[im+1][jm+1]==Figure.Black_Bishop || map[im+1][jm+1]==Figure.Black_Queen){return true;}
            else return false;
        }
        else return false;
    }
    private static boolean checkTopBlack(Figure[][] map, int im, int jm){
        if(im-1>=0){
            if(map[im-1][jm]==Figure.Empty){return checkTopBlack(map,im-1,jm);}
            else if(map[im-1][jm]==Figure.White_Rook || map[im-1][jm]==Figure.White_Queen){return true;}
            else return false;
        }
        else return false;
    }
    private static boolean checkBottomBlack(Figure[][] map, int im, int jm){
        if(im+1<8){
            if(map[im+1][jm]==Figure.Empty){return checkBottomBlack(map,im+1,jm);}
            else if(map[im+1][jm]==Figure.White_Rook || map[im+1][jm]==Figure.White_Queen){return true;}
            else return false;
        }
        else return false;
    }
    private static boolean checkRightBlack(Figure[][] map, int im, int jm){
        if(jm+1<8){
            if(map[im][jm+1]==Figure.Empty){return checkRightBlack(map,im,jm+1);}
            else if(map[im][jm+1]==Figure.White_Rook || map[im][jm+1]==Figure.White_Queen){return true;}
            else return false;
        }
        else return false;
    }
    private static boolean checkLeftBlack(Figure[][] map, int im, int jm){
        if(jm-1>=0){
            if(map[im][jm-1]==Figure.Empty){return checkLeftBlack(map,im,jm-1);}
            else if(map[im][jm-1]==Figure.White_Rook || map[im][jm-1]==Figure.White_Queen){return true;}
            else return false;
        }
        else return false;
    }
    private static boolean checkLeftTopBlack(Figure[][] map, int im, int jm){
        if(im-1>=0 && jm-1>=0){
            if(map[im-1][jm-1]==Figure.Empty){return checkLeftTopBlack(map,im-1,jm-1);}
            else if(map[im-1][jm-1]==Figure.White_Bishop || map[im-1][jm-1]==Figure.White_Queen){return true;}
            else return false;
        }
        else return false;
    }
    private static boolean checkRightTopBlack(Figure[][] map, int im, int jm){
        if(im-1>=0 && jm+1<8){
            if(map[im-1][jm+1]==Figure.Empty){return checkRightTopBlack(map,im-1,jm+1);}
            else if(map[im-1][jm+1]==Figure.White_Bishop || map[im-1][jm+1]==Figure.White_Queen){return true;}
            else return false;
        }
        else return false;
    }
    private static boolean checkLeftBottomBlack(Figure[][] map, int im, int jm){
        if(im+1<8 && jm-1>=0){
            if(map[im+1][jm-1]==Figure.Empty){return checkLeftBottomBlack(map,im+1,jm-1);}
            else if(map[im+1][jm-1]==Figure.White_Bishop || map[im+1][jm-1]==Figure.White_Queen){return true;}
            else return false;
        }
        else return false;
    }
    private static boolean checkRightBottomBlack(Figure[][] map, int im, int jm){
        if(im+1<8 && jm+1<8){
            if(map[im+1][jm+1]==Figure.Empty){return checkRightBottomBlack(map,im+1,jm+1);}
            else if(map[im+1][jm+1]==Figure.White_Bishop || map[im+1][jm+1]==Figure.White_Queen){return true;}
            else return false;
        }
        else return false;
    }
    private static boolean validateTile(Figure[][] map, int im, int jm, Players player){
        if(im>=0 && jm>=0 && im<8 && jm<8){
            switch (map[im][jm]){
                case Empty: return true;
                case White_Bishop: if(player == Players.Black){return true;}
                case White_Knight: if(player == Players.Black){return true;}
                case White_Pawn: if(player == Players.Black){return true;}
                case White_Queen: if(player == Players.Black){return true;}
                case White_Rook: if(player == Players.Black){return true;}
                case White_King: break;
                case Black_Bishop: if(player == Players.White){return true;}
                case Black_Knight: if(player == Players.White){return true;}
                case Black_Pawn: if(player == Players.White){return true;}
                case Black_Queen: if(player == Players.White){return true;}
                case Black_Rook: if(player == Players.White){return true;}
                case Black_King: break;
            }
        }
        return false;
    }
    private static void knightTurnValidate(Figure[][] map, Boolean[][] validMap, int im, int jm, Players player){
        if(im-2>=0){
            if(jm-1>=0){validMap[im-2][jm-1] = validateTile(map,im-2,jm-1,player);}
            if(jm+1<8){validMap[im-2][jm+1] = validateTile(map,im-2,jm+1,player);}
        }
        if(im+2<8){
            if(jm-1>=0){validMap[im+2][jm-1] = validateTile(map,im+2,jm-1,player);}
            if(jm+1<8){validMap[im+2][jm+1] = validateTile(map,im+2,jm+1,player);}
        }
        if(jm-2>=0){
            if(im-1>=0){validMap[im-1][jm-2] = validateTile(map,im-1,jm-2,player);}
            if(im+1<8){validMap[im+1][jm-2] = validateTile(map,im+1,jm-2,player);}
        }
        if(jm+2<8){
            if(im+1<8){validMap[im+1][jm+2] = validateTile(map,im+1,jm+2,player);}
            if(im-1>=0){validMap[im-1][jm+2] = validateTile(map,im-1,jm+2,player);}
        }

    }
    private static void bottomValidate(Figure[][] map, Boolean[][] validMap, int im, int jm, Players player){
        if(validateTile(map,im+1,jm,player)){
            validMap[im+1][jm]=true;
            if(map[im+1][jm]==Figure.Empty){bottomValidate(map,validMap,im+1,jm, player);}
        }
    }
    private static void topValidate(Figure[][] map, Boolean[][] validMap, int im, int jm, Players player){
        if(validateTile(map,im-1,jm,player)){
            validMap[im-1][jm]=true;
            if(map[im-1][jm]==Figure.Empty){topValidate(map,validMap,im-1,jm, player);}
        }
    }
    private static void rightValidate(Figure[][] map, Boolean[][] validMap, int im, int jm, Players player){
        if(validateTile(map,im,jm+1,player)){
            validMap[im][jm+1]=true;
            if(map[im][jm+1]==Figure.Empty){rightValidate(map,validMap,im,jm+1, player);}
        }
    }
    private static void leftValidate(Figure[][] map, Boolean[][] validMap, int im, int jm, Players player){
        if(validateTile(map,im,jm-1,player)){
            validMap[im][jm-1]=true;
            if(map[im][jm-1]==Figure.Empty){leftValidate(map,validMap,im,jm-1, player);}
        }
    }
    private static void rightBottomValidate(Figure[][] map, Boolean[][] validMap, int im, int jm, Players player){
        if(validateTile(map,im+1,jm+1,player)){
            validMap[im+1][jm+1]=true;
            if(map[im+1][jm+1]==Figure.Empty){rightBottomValidate(map,validMap,im+1,jm+1, player);}
        }
    }
    private static void rightTopValidate(Figure[][] map, Boolean[][] validMap, int im, int jm, Players player){
        if(validateTile(map,im-1,jm+1,player)){
            validMap[im-1][jm+1]=true;
            if(map[im-1][jm+1]==Figure.Empty){rightTopValidate(map,validMap,im-1,jm+1, player);}
        }
    }
    private static void leftTopValidate(Figure[][] map, Boolean[][] validMap, int im, int jm, Players player){
        if(validateTile(map,im-1,jm-1,player)){
            validMap[im-1][jm-1]=true;
            if(map[im-1][jm-1]==Figure.Empty){leftTopValidate(map,validMap,im-1,jm-1, player);}
        }
    }
    private static void leftBottomValidate(Figure[][] map, Boolean[][] validMap, int im, int jm, Players player){
        if(validateTile(map,im+1,jm-1,player)){
            validMap[im+1][jm-1]=true;
            if(map[im+1][jm-1]==Figure.Empty){leftBottomValidate(map,validMap,im+1,jm-1, player);}
        }
    }

    public static boolean discoverCheck(Figure[][] map, Players player){
        if(player == Players.White){
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    if(map[i][j]==Figure.White_King && isUnderAttack(map,i,j,player)) {
                        return true;
                    }
                }
            }
            return false;
        }
        else{
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    if(map[i][j]==Figure.Black_King && isUnderAttack(map,i,j,player)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    public static ArrayList<ChessTurn> discoverMate(ArrayList<ChessTurn> turnList, Figure[][] map, Players player, boolean blackKingCounter, boolean blackShortRookCounter, boolean whiteKingCounter, boolean whiteShortRookCounter, boolean blackLongRookCounter, boolean whiteLongRookCounter){
        ArrayList<ChessTurn> validTurns = new ArrayList<>();
        for(int i = 0; i<8;i++){
            for(int j=0;j<8;j++){
                Boolean[][] valids = findValidTurns(turnList, i,j,map,player, blackKingCounter, blackShortRookCounter, whiteKingCounter, whiteShortRookCounter, blackLongRookCounter, whiteLongRookCounter);
                for(int k = 0;k<8;k++){
                    for(int l=0;l<8;l++){
                        if(valids[k][l]){
                            ChessTurn t = new ChessTurn(j,i,l,k);
                            if(validateTurn(t, map,player)){
                                validTurns.add(t);
                            }
                        }
                    }
                }
            }
        }
        return validTurns;
    }
    public static boolean validateTurn(ChessTurn t, Figure[][] map, Players player){
        Figure[][] tempMap = new Figure[8][8];
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                tempMap[i][j]=map[i][j];
            }
        }
        tempMap[t.y_start][t.x_start] = Figure.Empty;
        tempMap[t.y_finish][t.x_finish] = map[t.y_start][t.x_start];
        return !discoverCheck(tempMap,player);
    }
}

//abstract class Figure{
//    void step(Turn turn) {
//        if(isCorrectTurn(turn)){
//
//        }
//    }
//    abstract boolean isCorrectTurn(Turn turn);
//
//    boolean isWhite;
//}

//class Pawn extends Figure{
//    @Override
//    boolean isCorrectTurn(Turn turn){
//        return true;
//    }
//}

