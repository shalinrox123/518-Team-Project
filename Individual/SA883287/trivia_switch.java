//dont know if you need a specific import for random
import java.util.*;

public String getTrivia(String iconName){

if (iconName.equalsIgnoreCase("01d.png") || iconName.equalsIgnoreCase("01n.png") ){
	return trivia("clear");
}
else if (iconName.equalsIgnoreCase("02d.png") || iconName.equalsIgnoreCase("02n.png" || iconName.equalsIgnoreCase("03d.png") || iconName.equalsIgnoreCase("03n.png") || iconName.equalsIgnoreCase("04d.png") || iconName.equalsIgnoreCase("04n.png")){
	return trivia("clouds");

}
else if (iconName.equalsIgnoreCase("09d.png") || iconName.equalsIgnoreCase("09n.png") || iconName.equalsIgnoreCase("10d.png") || iconName.equalsIgnoreCase("10n.png")||iconName.equalsIgnoreCase("10d.png") || iconName.equalsIgnoreCase("10n.png")){
	return trivia("rain");

}
else if (iconName.equalsIgnoreCase("11d.png") || iconName.equalsIgnoreCase("11n.png") ){
	return trivia("thunder");

}
else if (iconName.equalsIgnoreCase("13d.png") || iconName.equalsIgnoreCase("13n.png") ){
	return trivia("snow");

}
else if (iconName.equalsIgnoreCase("50d.png") || iconName.equalsIgnoreCase("50n.png") ){
	return trivia("mist");

}
else
return "Enjoy your day";
}

public String trivia(String des){
	Random r = new Random(); 
	int num = r.nextInt(3);

	switch (des)
	{
     case "clear":
	     switch (num)
		{
	     case 1:
	     	return "The sky is blue because of the way sunlight interacts with our atmosphere.";
	     break;
	     case 2:
	     	return "You can see the reflection of the moon in the sky sometimes.";
	     //Java code
	     break;
	     case 0:
	     	return "Clear Skies don't indicate a clear day";
	     //Java code
	     break;
	     default:
	     	return "Enjoy the day";
	     break;
		}
     break;
     case "clouds":
	     switch (num)
		{
	     case 1:
	     return "A cloud is a large group of tiny water droplets that we can see in the air.";
	     break;
	     case 2:
	     return "Clouds can contain millions of tons of water.";
	     //Java code
	     break;
	     case 0:
	     return "There are many variations of these 3 main cloud types including stratocumulus, altostratus, altocumulus, cirrostratus and cirrocumulus.";
	     //Java code
	     break;
	     default:
	     return "Enjoy the day";
	     break;
		}
     //Java code
     break;
     case "rain":
      switch (num)
		{
	     case 1:
	     	return "With an average cloud height of 2,500 feet and an average speed of 14mph, it would take two minutes for a raindrop to hit the floor.";
	     	break;
	     case 2:
	     	return "An inch of rain can measure around 226,000 lbs in an acre of land. In every minute, one billion tons of rain falls on the earth.";
	     //Java code
	    	 break;
	     case 0:
	     	return "Water vapor turns into clouds when it cools and condenses—that is, turns back into liquid water or ice. Within a cloud, water droplets condense onto one another, causing the droplets to grow. When these water droplets get too heavy to stay suspended in the cloud, they fall to Earth as rain.";
	     //Java code
	     	break;
	     default:
	     	return "Enjoy the day";
	     break;
		}
     //Java code
     break;
     case "thunder":
	     switch (num)
			{
		     case 1:
		     	return "Thunder is the sound caused by lightning. Because sound travels much slower than light, you'll always see a flash of lightning before you hear any thunder.";
		     	break;
		     case 2:
		     	return "A clap of thunder registers around 120 decibels.";
		     //Java code
		    	 break;
		     case 0:
		     	return "It's impossible to have thunder without a lightning bolt, since thunder is produced from a lightning bolt. However, if you're far enough away from a lightning bolt you'll see the flash, but not hear the thunder.";
		     //Java code
		     	break;
		     default:
		     	return "Enjoy the day";
		     break;
			}
     //Java code
     break;
     case "snow":
     	switch (num)
		{
	     case 1:
	     	return "In 1988, a scientist found two identical snow crystals. They came from a storm in Wisconsin.";
	     	break;
	     case 2:
	     	return "Freshly fallen snow absorbs sound waves, giving everything a seemingly hushed, quieter ambience after a flurry. But if the snow then melts and refreezes, the ice can reflect sound waves making sound travel further and clearer.";
	     //Java code
	    	 break;
	     case 3:
	     	return "Snow forms when water vapor in the atmosphere freezes into ice crystals.";
	     //Java code
	     	break;
	     default:
	     	return "Enjoy the day";
	     break;
		}
     //Java code
     break;
     case "mist":
     	     	switch (num)
		{
	     case 1:
	     	return "Mist often forms when warmer air over water suddenly encounters the cooler surface of land.";
	     	break;
	     case 2:
	     	return "Mist is seen where warm, moist air meets sudden cooling, such as in exhaled air in the winter, or when throwing water onto the hot stove of a sauna.";
	     //Java code
	    	 break;
	     case 0:
	     	return "Misty is a character in Pokémon.";
	     //Java code
	     	break;
	     default:
	     	return "Enjoy the day";
	     break;
		}
     //Java code
     break;
     default:
     	return "Enjoy the day";
     break;
	} 

}
