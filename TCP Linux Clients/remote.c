#include<string.h>
#include<stdio.h>		//printf
#include<string.h>		//strlen
#include<stdlib.h> 		//system()

int main (int argc, char *argv[])
{

	if (argc != 3)
    {
      fprintf (stderr, "Error: ./remote <device> <function> \n");
      return 1;
    }
	
	char * arrOfTVCommands[] = 
	{
		  "irsend SEND_ONCE TV KEY_POWER", 			//index 0
		  "irsend SEND_ONCE TV KEY_VOLUMEUP", 		//index 1
		  "irsend SEND_ONCE TV KEY_VOLUMEDOWN"		//index 2
	};
	
	char * arrOfSoundBarCommands[] = 
	{
		"irsend SEND_ONCE SOUNDBAR KEY_POWER", 		//index 0
		"irsend SEND_ONCE SOUNDBAR KEY_VOLUMEUP", 	//index 1
		"irsend SEND_ONCE SOUNDBAR KEY_VOLUMEDOWN", //index 2
		"irsend SEND_ONCE SOUNDBAR KEY_S", 			//index 3
		"irsend SEND_ONCE SOUNDBAR KEY_MUTE", 		//index 4
		"irsend SEND_ONCE SOUNDBAR KEY_LEFT", 		//index 5
		"irsend SEND_ONCE SOUNDBAR KEY_RIGHT", 		//index 6
		"irsend SEND_ONCE SOUNDBAR KEY_NEXT", 		//index 7
		"irsend SEND_ONCE SOUNDBAR KEY_PLAY", 		//index 8
		"irsend SEND_ONCE SOUNDBAR KEY_PREVIOUS",	//index 9
		"irsend SEND_ONCE SOUNDBAR KEY_SHUFFLE", 	//index 10
		"irsend SEND_ONCE SOUNDBAR KEY_FN1", 		//index 11
		"irsend SEND_ONCE SOUNDBAR KEY_FN2", 		//index 12
		"irsend SEND_ONCE SOUNDBAR KEY_FN3", 		//index 13
		"irsend SEND_ONCE SOUNDBAR KEY_FN3"			//index 14
	};
	
	if (strcmp(argv[1], "tv") == 0)
	{
		if (strcmp(argv[2], "power") == 0)
			system(arrOfTVCommands[0]);
		
		else if (strcmp(argv[2], "volup") == 0)
			system(arrOfTVCommands[1]);
		
		else if (strcmp(argv[2], "voldown") == 0)
			system(arrOfTVCommands[2]);
		else
			printf("Error: Invalid Function Name!\n");  
	}
	
	else if (strcmp(argv[1], "soundbar") == 0)
	{
		if (strcmp(argv[2], "power") == 0)
			system(arrOfTVCommands[0]);
		
		else if (strcmp(argv[2], "volup") == 0)
			system(arrOfTVCommands[1]);
		
		else if (strcmp(argv[2], "voldown") == 0)
			system(arrOfTVCommands[2]);
		
		else if (strcmp(argv[2], "source") == 0)
			system(arrOfTVCommands[3]);
		
		else if (strcmp(argv[2], "mute") == 0)
			system(arrOfTVCommands[4]);
		
		else if (strcmp(argv[2], "left") == 0)
			system(arrOfTVCommands[5]);
		
		else if (strcmp(argv[2], "right") == 0)
			system(arrOfTVCommands[6]);
		
		else if (strcmp(argv[2], "next") == 0)
			system(arrOfTVCommands[7]);
		
		else if (strcmp(argv[2], "play") == 0)
			system(arrOfTVCommands[8]);
		
		else if (strcmp(argv[2], "pre") == 0)
			system(arrOfTVCommands[9]);
		
		else if (strcmp(argv[2], "effect") == 0)
			system(arrOfTVCommands[10]);
		
		else if (strcmp(argv[2], "sound") == 0)
			system(arrOfTVCommands[11]);
		
		else if (strcmp(argv[2], "bluetooth") == 0)
			system(arrOfTVCommands[12]);
		
		else if (strcmp(argv[2], "control") == 0)
			system(arrOfTVCommands[13]);
		else
			printf("Error: Invalid Function Name!\n");  
	}
	
	else
		printf("Error: Invalid Device Name!\n");  
	
	
	return 0;
}

