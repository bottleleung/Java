package traingame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 游戏场景类
 * @author Leslie Leung
 */
public class TrainGame extends JPanel {
	public static final int PLAYER_ME = 0;	//玩家自己
	public static final int PLAYER_COMPUTER = 1;	//系统玩家
	public static final int DEFAULT_PLAYER = PLAYER_ME;		//默认首先发牌给玩家自己
	public static final int CARD_NUM = 52;		//纸牌的数目
	public static final int OWN_TIME = 10000;	//玩家自己出牌时可以掌控的时间
	public static final int INTERVAL = 1000;	//玩家自己meCountDown时间间隔
	/* 用于存储各种花式的数组 */
	public static final int[] ARR_SUIT = {Card.SUIT_SPADE, Card.SUIT_HEART, Card.SUIT_DIAMOND, Card.SUIT_CLUB};
	/* 用于存储各种点数的数组 */
	public static final int[] ARR_POINT = {Card.POINT_A, Card.POINT_2, Card.POINT_3, Card.POINT_4, Card.POINT_5,
		Card.POINT_6, Card.POINT_7, Card.POINT_8, Card.POINT_9, Card.POINT_10, Card.POINT_J, Card.POINT_Q, Card.POINT_K};

	/* 定义10个静态变量 */
	private static int xCoordinateOfMyCard = 100;	//玩家自己纸牌的横坐标
	private static int yCoordinateOfMyCard = 500;	//玩家自己纸牌的纵坐标
	private static int xCoordinateOfComputerCard = 100;		//系统玩家纸牌的横坐标
	private static int yCoordinateOfComputerCard = 50;		//系统玩家纸牌的纵坐标
	private static int xCoordinateOfTrainCard = 100;	//火车上纸牌的横坐标
	private static int yCoordinateOfTrainCard = 300;	//火车上纸牌的纵坐标
	private static int xCoordinateOfAllCards = 10;	//总纸牌列表的横坐标
	private static int yCoordinateOfAllCards = 10;	//总纸牌列表的纵坐标
	private static int offsetBetweenCards = 10;		//一个集合中纸牌之间的距离偏移
	private static int timeLeftForPlayerMe = 10;	//玩家自己剩余的出牌时间

	private List<Card> cards;	//表示所有牌
	private List<Card> train;	//表示火车
	private Map<Integer, List<Card>> players;	//表示所有玩家和它所拥有的纸牌
	private int turn;	//表示轮到把牌发给哪个玩家
	private Timer dealTimer;	//用以处理发牌的计时器
	private Timer playTimer;	//用以处理整个游戏过程的计时器
	private Timer meCountDown;		//实现玩家自己出牌的倒计时
	private boolean oneCardSelected;	//表示某张纸牌是否被选中
	private MouseControl mc;	//实现鼠标监控的内部类的对象引用

	/**
	 * 构造方法
	 */
	public TrainGame() {
		setPreferredSize(new Dimension(800, 650));		//设置游戏界面大小

		/* 初始化 */
		cards = new LinkedList<Card>();
		train = new LinkedList<Card>();
		players = new HashMap<Integer, List<Card>>();
		turn = DEFAULT_PLAYER;
		dealTimer = new Timer();
		playTimer = new Timer();
		oneCardSelected = false;	//表示某张纸牌没被选中
		mc = new MouseControl();	//新建内部类对象


		/* 生成所有纸牌 */
		for(int i = 0; i < ARR_SUIT.length; i ++) {
			for(int j = 0; j < ARR_POINT.length; j ++) {
				cards.add(new Card(ARR_POINT[j], ARR_SUIT[i]));
			}
		}

		Collections.shuffle(cards);	//洗牌

		/* 设置所有纸牌的横坐标和纵坐标 */
		for(int i = 0; i < cards.size(); i ++) {
			cards.get(i).setX(xCoordinateOfAllCards);
			cards.get(i).setY(yCoordinateOfAllCards);
			yCoordinateOfAllCards += offsetBetweenCards;
		}

		/* 新建两个玩家 */
		players.put(PLAYER_ME, Collections.synchronizedList(new LinkedList<Card>()));
		players.put(PLAYER_COMPUTER, Collections.synchronizedList(new LinkedList<Card>()));

		dealTimer.schedule(new DealExecution(), 30, 80);
	}

	/**
	 * 内部类，用以监听鼠标事件
	 * @author Leslie Leung
	 */
	private class MouseControl extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();

			if(e.getButton() == MouseEvent.BUTTON1) {//单击纸牌时的操作
				findTrainCardByXYAndSetSelected(x, y);	//找到目标纸牌位置并设置其是否选中
			}

			if(e.getClickCount() == 2) {//发完牌后，双击纸牌时的操作

				List<Card> playerCardsList = getPlayerCards(PLAYER_ME);		//获得玩家自己的纸牌集合
				Card targetCard = playerCardsList.get(0);	//目标纸牌，玩家自己的第一张纸牌

				/* 当双击中玩家自己第一张纸牌时 */
				if(playerCardsList.size() > 1 && x > targetCard.getX() && x < targetCard.getX() + offsetBetweenCards &&
						y > targetCard.getY() && y < targetCard.getY() + Card.CARD_HEIGHT) {

					play(PLAYER_ME);	//玩家自己开始玩

					/* 如果玩家自己一出牌，马上停止playTimer计时，操作权转移给系统并重新新建playerTimer */
					playTimer.cancel();
					turn = PLAYER_COMPUTER;
					playTimer = new Timer();
					playTimer.schedule(new PlayExecution(), 0, OWN_TIME);				
				} 
				/* 当双击中玩家自己最后一张纸牌的时候 */
				else if(playerCardsList.size() == 1 && x > targetCard.getX() && x < targetCard.getX() + Card.CARD_WIDTH &&
						y > targetCard.getY() && y < targetCard.getY() + Card.CARD_HEIGHT) {

					play(PLAYER_ME);	//玩家自己开始玩

					/* 如果玩家自己一出牌，马上停止playTimer计时，操作权转移给系统并重新新建playerTimer */
					playTimer.cancel();
					turn = PLAYER_COMPUTER;
					playTimer = new Timer();
				}
			}

			repaint();
		}
	}

	/**
	 * 根据横坐标和纵坐标找到火车上目标纸牌的位置并设置其是否被选中
	 * @param x 面板中某点的横坐标
	 * @param y 面板中某点的纵坐标
	 */
	public void findTrainCardByXYAndSetSelected(int x, int y) {
		for(int i = 0; i < train.size(); i ++) {
			/* 目标纸牌为火车上除最后一张纸牌外的任意一张牌 */
			if(i < train.size() - 1 && x > getTrainCard(i).getX() && x < getTrainCard(i).getX() + offsetBetweenCards &&
					y > getTrainCard(i).getY() && y < getTrainCard(i).getY() + Card.CARD_HEIGHT) {

				setTrainCardSelected(i);
				break;
			} 
			/* 目标纸牌为火车上最后一张纸牌 */
			else if(i == train.size() - 1 && x > getTrainCard(i).getX() && x < getTrainCard(i).getX() + Card.CARD_WIDTH &&
					y > getTrainCard(i).getY() && y < getTrainCard(i).getY() + Card.CARD_HEIGHT){

				setTrainCardSelected(i);
				break;
			}
		}

	}

	/**
	 * 根据火车上某纸牌是否被选中设置其选中状态
	 * @param i 火车上某张纸牌的下标
	 */
	public void setTrainCardSelected(int i) {
		if(!getTrainCard(i).isSelected() && !oneCardSelected) {
			getTrainCard(i).setSelected(true);
			oneCardSelected = true;
		} else if(getTrainCard(i).isSelected() && oneCardSelected) {
			getTrainCard(i).setSelected(false);
			oneCardSelected = false;
		}
	}

	/**
	 * 获取火车上的某张纸牌
	 * @param i 集合中的下标
	 * @return 目标纸牌
	 */
	public Card getTrainCard(int i) {
		return train.get(i);
	}

	/**
	 * 内部类，实现游戏运行操作
	 * @author Leslie Leung
	 */
	private class PlayExecution extends TimerTask {	
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(turn == PLAYER_ME) {//如果由玩家自己出牌

				meCountDown = new Timer();	//初始化meCountDown			
				timeLeftForPlayerMe = 10;	//每次轮到玩家自己时，重设倒计时			
				addMouseListener(mc);		//添加事件监控
				
				meCountDown.schedule(new TimerTask() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(timeLeftForPlayerMe > 0 && timeLeftForPlayerMe <= 10) {
							repaint();
							timeLeftForPlayerMe --;	//倒计时

							if(timeLeftForPlayerMe == 0) {
								play(PLAYER_ME);	//当玩家自己出牌剩余时间为0时，自动出牌
								turn = PLAYER_COMPUTER;		//把操作权转交给系统玩家							
							}

						}
					}
				}, 0, INTERVAL);

			} else if(turn == PLAYER_COMPUTER) {//如果由系统玩家出牌

				meCountDown.cancel();
				removeMouseListener(mc);	//移除监听器

				autoFindTrainCardAndSetSelected();	//自动找到火车上与系统玩家将要出的牌点数相同的牌并将其选中
				play(PLAYER_COMPUTER);	//系统玩家开始游戏
				repaint();
				
				/* 系统玩家纸牌大于0才把操作权转移给玩家自己 */
				if(getPlayerCards(PLAYER_COMPUTER).size() > 0) {
					/* 如果系统玩家一出牌，马上停止playTimer计时，操作权转移给玩家自己并重新新建playTimer */
					playTimer.cancel();
					turn = PLAYER_ME;		//把操作权转交给玩家自己
					playTimer = new Timer();
					playTimer.schedule(new PlayExecution(), 0, OWN_TIME);
				}
						
			}

		}
	}

	/**
	 * 内部类，用以实现发牌操作
	 * @author Leslie Leung
	 */
	private class DealExecution extends TimerTask {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(cards.size() == 0) {

				dealTimer.cancel();		//如果牌已经发完，终止计时，游戏开始
				turn = PLAYER_ME;	//刚开始时先由玩家自己操作
				playTimer.schedule(new PlayExecution(), 0, OWN_TIME);	//游戏真正开始

			} else {
				startDealing();		//开始发牌
			}
			repaint();
		}			
	}

	/**
	 * 自动找到火车上一张与系统玩家将要出的牌点数一样的纸牌并设置其为选中
	 */
	public void autoFindTrainCardAndSetSelected() {
		for(int i = 0; i < train.size(); i ++) {
			/* 如果火车上某一张纸牌的点数与系统玩家将要出牌的点数一样，把火车上的这张牌设置为选中 */
			if(getTrainCard(i).getPoint() == getPlayerCards(PLAYER_COMPUTER).get(0).getPoint()) {
				getTrainCard(i).setSelected(true);
			}
		}
	}

	/**
	 * 根据轮转次序调用发牌算法发牌
	 */
	public void startDealing() {
		if(turn == PLAYER_ME) {//如果轮到玩家自己

			Card c = cards.remove(cards.size() - 1);//移除纸牌总列表中的一张
			setXOfMyCards(c);   //设置玩家自己纸牌的横坐标
			setYOfMyCards(c);   //设置玩家自己纸牌的纵坐标

			deal(turn, c);		//发牌
			turn = PLAYER_COMPUTER;		//把次序转到系统玩家

		} else if(turn == PLAYER_COMPUTER) {//如果轮到系统玩家

			Card c = cards.remove(cards.size() - 1);//移除纸牌总列表中的一张
			setXOfComputerCards(c);		//设置系统玩家纸牌的横坐标
			setYOfComputerCards(c);		//设置系统玩家纸牌的纵坐标

			deal(turn, c);		//发牌
			turn = PLAYER_ME;	//把次序转到玩家自己
		}
	}

	/**
	 * 设置玩家自己纸牌的横坐标
	 * @param card 玩家自己的某张纸牌
	 */
	public void setXOfMyCards(Card card) {
		card.setX(xCoordinateOfMyCard);	
		xCoordinateOfMyCard += offsetBetweenCards;
	}

	/**
	 * 设置玩家自己纸牌的纵坐标
	 * @param card 玩家自己的某张纸牌
	 */
	public void setYOfMyCards(Card card) {
		card.setY(yCoordinateOfMyCard);
	}

	/**
	 * 设置系统玩家纸牌的横坐标
	 * @param card 系统玩家的某张纸牌
	 */
	public void setXOfComputerCards(Card card) {
		card.setX(xCoordinateOfComputerCard);
		xCoordinateOfComputerCard += offsetBetweenCards;
	}

	/**
	 * 设置系统玩家纸牌的纵坐标
	 * @param card 系统玩家的某张纸牌
	 */
	public void setYOfComputerCards(Card card) {
		card.setY(yCoordinateOfComputerCard);
	}

	/**
	 * 设置火车纸牌的横坐标
	 * @param card 火车上的某张纸牌
	 */
	public void setXOfTrainCard(Card card) {
		card.setX(xCoordinateOfTrainCard);	
		xCoordinateOfTrainCard += offsetBetweenCards;
	}

	/**
	 * 设置火车纸牌的纵坐标
	 * @param card 火车上的某张纸牌
	 */
	public void setYOfTrainCard(Card card) {
		card.setY(yCoordinateOfTrainCard);
	}

	/**
	 * 发牌算法
	 * @param player 指定的玩家
	 * @param card 要发给玩家的纸牌
	 */
	public void deal(int player, Card card) {
		getPlayerCards(player).add(card);	//把纸牌添加到玩家纸牌列表
		card.setFace(true);		//设置纸牌正面朝上
	}

	/**
	 * 某个玩家开始其游戏过程
	 * @param player 玩家
	 */
	public void play(int player) {
		List<Card> playerCards = getPlayerCards(player);

		Card card = playerCards.remove(0);	//出牌，规定出该玩家的第一张纸牌
		train.add(card);	//把纸牌添加到火车中
		setXOfTrainCard(card);	//设置该纸牌的横坐标
		setYOfTrainCard(card);	//设置该纸牌的纵坐标

		resetXOfPlayerCard(player);		//重设该玩家纸牌的横坐标
		/* 收牌 */
		for(int i = 0; i < train.size(); i ++) {
			if(getTrainCard(i).isSelected()) {//如果火车中某张牌被选中
				getTrainCard(i).setSelected(false);		//取消选中
				oneCardSelected = false;	//火车上没有纸牌被选中

				/* 如果该牌被选中且其点数与所出的牌相同，收牌 */
				if(getTrainCard(i).getPoint() == card.getPoint()) {
					reap(player, getTrainCard(i), i);
				}
				break;
			}
		}

		/* 根据该玩家的牌数是否为0判断输赢 */
		if(getPlayerCards(player).size() == 0) {
			if(player == PLAYER_COMPUTER) {//如果该玩家是系统
				JOptionPane.showMessageDialog(null, "恭喜你，你赢了");

				removeMouseListener(mc);
				playTimer.cancel();
				meCountDown.cancel();
			} else if(player == PLAYER_ME) {//如果该玩家是玩家自己
				JOptionPane.showMessageDialog(null, "你输了");

				removeMouseListener(mc);
				playTimer.cancel();
				meCountDown.cancel();
			}
		}

	}

	/**
	 * 重设某个玩家的纸牌横坐标
	 * @param player
	 */
	public void resetXOfPlayerCard(int player) {
		List<Card> resetCards = getPlayerCards(player);

		if(player == PLAYER_ME) {//如果是玩家自己
			xCoordinateOfMyCard = 100;

			for(int i = 0; i < resetCards.size(); i ++) {//重设玩家自己所有纸牌的横坐标
				setXOfMyCards(resetCards.get(i));	
			}

		} else if(player == PLAYER_COMPUTER) {//如果是系统玩家
			xCoordinateOfComputerCard = 100;

			for(int i = 0; i < resetCards.size(); i ++) {//重设系统玩家所有纸牌的横坐标
				setXOfComputerCards(resetCards.get(i));	
			}
		}
	}

	/**
	 * 获取某个玩家的所有纸牌
	 * @param player 需要获取的玩家
	 * @return 该玩家的所有纸牌
	 */
	public List<Card> getPlayerCards(int player) {
		return players.get(player);
	}

	/**
	 * 收牌算法
	 * @param player 玩家
	 * @param card 该玩家出的牌
	 * @param i 火车上开始收牌的位置的下标
	 */
	public void reap(int player, Card card, int i) {
		List<Card> subList = train.subList(i, train.size());	//获取两张点数相同的牌中间的所有纸牌

		/* 重设火车上纸牌的横坐标 */
		for(int j = 0; j < subList.size(); j ++) {
			xCoordinateOfTrainCard -= offsetBetweenCards;
		}

		oneCardSelected = false;	//火车上没有纸牌被选中
		addToPlayer(player, subList);	//把纸牌添加到玩家中
		subList.clear();	//在火车集合中把收掉的纸牌移除
	}

	/**
	 * 把收回的纸牌添加到玩家中
	 * @param player 玩家
	 * @param cards 收回的纸牌的集合
	 */
	public void addToPlayer(int player, List<Card> cards) {
		getPlayerCards(player).addAll(cards);	//把牌添加到玩家中

		if(player == PLAYER_ME) {//如果是玩家自己

			for(int i = 0; i < cards.size(); i ++) {//重设添加到玩家自己集合的所有纸牌的横坐标
				setXOfMyCards(cards.get(i));
				setYOfMyCards(cards.get(i));
			}

		} else if(player == PLAYER_COMPUTER) {//如果是系统玩家

			for(int i = 0; i < cards.size(); i ++) {//重设添加到玩家自己集合的所有纸牌的横坐标
				setXOfComputerCards(cards.get(i));
				setYOfComputerCards(cards.get(i));
			}

		}
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getBounds().width, getBounds().height);

		/* 绘制所有纸牌 */
		for(int i = 0; i < cards.size(); i++) {
			cards.get(i).paintCard(g);			
		}

		/* 绘制玩家自己的纸牌 */
		for(int i = 0; i < getPlayerCards(PLAYER_ME).size(); i ++) {
			getPlayerCards(PLAYER_ME).get(i).paintCard(g);
		}

		/* 绘制系统玩家的纸牌 */
		for(int i = 0; i < getPlayerCards(PLAYER_COMPUTER).size(); i ++) {
			getPlayerCards(PLAYER_COMPUTER).get(i).paintCard(g);
		}

		/* 绘制火车 */
		for(int i = 0; i < train.size(); i ++) {
			getTrainCard(i).paintCard(g);
		}

		/* 显示倒计时 */
		g.setColor(Color.BLUE);
		g.setFont(new Font("Serif",Font.BOLD|Font.ITALIC,24));
		g.drawString(timeLeftForPlayerMe + "", 400, 400);
	}	
}
