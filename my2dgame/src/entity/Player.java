package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {
	GamePanel gp;
	KeyHandler keyH;
	
	public final int screenX;
	public final int screenY;
	int hasKey = 0;

	public Player(GamePanel gp, KeyHandler keyH) {
		this.gp = gp;
		this.keyH = keyH;
		
		screenX = gp.screenWidth/2 -(gp.tileSize/2);
		screenY = gp.screenHeight/2 -(gp.tileSize/2);
		
		solidArea = new Rectangle();
		solidArea.x = 0;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x; 
		solidAreaDefaultY = solidArea.y; 
		solidArea.width = 32;
		solidArea.height = 32;

		setDefaultValues();
		getPlayerImage();

	}

	public void setDefaultValues() {
		worldX = gp.tileSize * 23;
		worldY = gp.tileSize * 21;
		speed = 4;
		direction = "down";
	}

	public void getPlayerImage() {
		try {
			up1 = ImageIO.read(getClass().getResourceAsStream("/player/Hamtaro-BackLeft.png"));
			up2 = ImageIO.read(getClass().getResourceAsStream("/player/Hamtaro-BackRight.png"));
			down1 = ImageIO.read(getClass().getResourceAsStream("/player/Hamtaro-FL.png"));
			down2 = ImageIO.read(getClass().getResourceAsStream("/player/Hamtaro-FR.png"));
			left1 = ImageIO.read(getClass().getResourceAsStream("/player/Hamtaro-LeftStand.png"));
			left2 = ImageIO.read(getClass().getResourceAsStream("/player/Hamtaro-LeftStep.png"));
			right1 = ImageIO.read(getClass().getResourceAsStream("/player/Hamtaro-RightStand.png"));
			right2 = ImageIO.read(getClass().getResourceAsStream("/player/Hamtaro-RightStep.png"));

		} catch(IOException e) {
			e.printStackTrace();
		}


	}

	public void update() {

		if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

			if (keyH.upPressed) {
				direction = "up";
			}
			else if (keyH.downPressed) {
				direction = "down";
			}
			else if (keyH.leftPressed) {
				direction = "left";
			}
			else if (keyH.rightPressed) {
				direction = "right";
			}
			
			collisionOn = false;
			gp.cChecker.checkTile(this);
			
			int objIndex = gp.cChecker.checkObject(this, true);
			pickupObject(objIndex);
			
			if (collisionOn == false) {
				switch(direction) {
				case "up":
					worldY -= speed;
					break;
				case "down":
					worldY += speed;
					break;
				case "left":
					worldX -= speed;
					break;
				case "right":
					worldX += speed;
					break;
			}
				
			}
			

			spriteCounter++;
			if (spriteCounter > 12) {
				if (spriteNum == 1) {
					spriteNum = 2;
				}
				else if (spriteNum == 2) {
					spriteNum = 1;
				}
				spriteCounter = 0;
			}
		}

	}
	
	public void pickupObject(int i) {
		if (i != 999) {
			String objectName = gp.obj[i].name;
			
			switch(objectName) {
			case "Key":
				gp.playSE(1);
				hasKey++;
				gp.obj[i]=null;
				break;
			case "Door":
				if (hasKey > 0) {
					gp.playSE(3);
					gp.obj[i] = null;
					hasKey--;
				}
				break;
			case "Boots": 
				gp.playSE(2);
				speed+=2;
				gp.obj[i] = null;
				break;
			}
		}
	}

	public void draw(Graphics2D g2) {
//		g2.setColor(Color.white);
//		g2.fillRect(x, y, gp.tileSize, gp.tileSize);

		BufferedImage image = null;
		switch(direction) {
		case "up":
			if (spriteNum == 1) {
				image = up1;
			}
			if (spriteNum == 2) {
				image = up2;
			}
			break;
		case "down":
			if (spriteNum == 1) {
				image = down1;
			}
			if (spriteNum == 2) {
				image = down2;
			}
			break;
		case "left":
			if (spriteNum == 1) {
				image = left1;
			}
			if (spriteNum == 2) {
				image = left2;
			}
			break;
		case "right":
			if (spriteNum == 1) {
				image = right1;
			}
			if (spriteNum == 2) {
				image = right2;
			}
			break;
		}
		g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

	}

}
