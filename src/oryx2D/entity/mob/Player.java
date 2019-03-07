package oryx2D.entity.mob;

import flash.display.BitmapData;
import oryx2D.Game;
import oryx2D.graphics.Screen;
import oryx2D.input.Keyboard;
import oryx2D.input.Mouse;

public class Player extends Mob {

    private static BitmapData player_forward = new BitmapData("/oryx2D/textures/player/player_forward.png");
    private static BitmapData player_forward_1 = new BitmapData("/oryx2D/textures/player/player_forward1.png");
    private static BitmapData player_forward_2 = new BitmapData("/oryx2D/textures/player/player_forward2.png");

    private static BitmapData player_back = new BitmapData("/oryx2D/textures/player/player_back.png");
    private static BitmapData player_back_1 = new BitmapData("/oryx2D/textures/player/player_back1.png");
    private static BitmapData player_back_2 = new BitmapData("/oryx2D/textures/player/player_back2.png");

    private static BitmapData player_side = new BitmapData("/oryx2D/textures/player/player_side.png");
    private static BitmapData player_side_1 = new BitmapData("/oryx2D/textures/player/player_side1.png");

    private boolean walking = false;
    private Keyboard input;
    private BitmapData sprite;
    private int anim = 0;

    public Player(int x, int y, Keyboard input) {
        this.x = x;
        this.y = y;
        this.input = input;
    }

    @Override
    public void update() {
        int xa = 0;
        int ya = 0;

        if (this.anim < 7500) {
            this.anim++;
        } else {
            this.anim = 0;
        }

        if (this.input.up) {
            ya--;
        }
        if (this.input.down) {
            ya++;
        }
        if (this.input.left) {
            xa--;
        }
        if (this.input.right) {
            xa++;
        }

        if ((xa != 0) || (ya != 0)) { //If we move
            this.move(xa, ya);
            this.walking = true;
        } else {
            this.walking = false;
        }

        this.updateShooting();
    }

    private void updateShooting() { //get angle of shooting based of center of screen
        if (Mouse.getButton() == 1) {
            double dx = Mouse.getX() - (Game.getWindowWidth() / 2);
            double dy = Mouse.getY() - (Game.getWindowHeight() / 2);
            double dir = Math.atan2(dy, dx);
            this.shoot(this.x, this.y, dir);
        }
    }

    @Override
    public void render(Screen screen) {
        int flip = 0;
        if (this.dir == 0) {
            this.sprite = player_forward;
            if (this.walking) {
                if ((this.anim % 20) > 10) {
                    this.sprite = player_forward_1;
                } else {
                    this.sprite = player_forward_2;
                }
            }
        } else if (this.dir == 2) {
            this.sprite = player_back;
            if (this.walking) {
                if ((this.anim % 20) > 10) {
                    this.sprite = player_back_1;
                } else {
                    this.sprite = player_back_2;
                }
            }
        } else {
            if (this.dir == 3) {
                flip = 1;
            }
            this.sprite = player_side;
            if (this.walking) {
                if ((this.anim % 20) > 10) {
                    this.sprite = player_side_1;
                } else {
                    this.sprite = player_side;
                }
            }
        }

        screen.render(this.x - (this.sprite.width / 2), this.y - (this.sprite.height / 2), this.sprite, flip);
    }

}
