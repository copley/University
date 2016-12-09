import ecs100.*;

public class Food
{
    private FoodType type;
    private boolean hasBeenMade;

    public Food( FoodType t )
    {
        type = t;
        hasBeenMade = false;
    }

    public void draw(int x, int y)
    {
        if( !hasBeenMade )
        {
            if (type == FoodType.FISH) UI.drawImage("Fish-grey.png", x, y);
            if (type == FoodType.CHIPS) UI.drawImage("Chips-grey.png", x, y);
            if (type == FoodType.BURGER) UI.drawImage("Burger-grey.png", x, y);
            if (type == FoodType.HOTDOG) UI.drawImage("Hotdog-grey.png", x, y);
            if (type == FoodType.SMOOTHIE) UI.drawImage("Smoothie-grey.png", x, y);
        }
        else
        {
            if (type == FoodType.FISH) UI.drawImage("Fish.png", x, y);
            if (type == FoodType.CHIPS) UI.drawImage("Chips.png", x, y);
            if (type == FoodType.BURGER) UI.drawImage("Burger.png", x, y);
            if (type == FoodType.HOTDOG) UI.drawImage("Hotdog.png", x, y);
            if (type == FoodType.SMOOTHIE) UI.drawImage("Smoothie.png", x, y);
        }
    }
    
    public double getPrice()
    {
        return FastFood.prices.get(type);
    }
    
    public FoodType getType()
    {
        return type;
    }
    
    public boolean isFoodReady()
    {
        return hasBeenMade;
    }
    
    public void foodIsReady()
    {
        hasBeenMade = true;
    }
}
