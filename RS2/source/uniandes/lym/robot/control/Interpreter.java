package uniandes.lym.robot.control;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import uniandes.lym.robot.kernel.*;



/**
 * Receives commands and relays them to the Robot. 
 */

public class Interpreter   {

	/**
	 * Robot's world
	 */
	private RobotWorldDec world;   


	public Interpreter()
	{
	}


	/**
	 * Creates a new interpreter for a given world
	 * @param world 
	 */


	public Interpreter(RobotWorld mundo)
	{
		this.world =  (RobotWorldDec) mundo;

	}


	/**
	 * sets a the world
	 * @param world 
	 */

	public void setWorld(RobotWorld m) 
	{
		world = (RobotWorldDec) m;

	}



	/**
	 *  Processes a sequence of commands. A command is a letter  followed by a ";"
	 *  The command can be:
	 *  M:  moves forward
	 *  R:  turns right
	 *  
	 * @param input Contiene una cadena de texto enviada para ser interpretada
	 */

	public String process(String input) throws Error
	{   


		StringBuffer output=new StringBuffer("SYSTEM RESPONSE: -->\n");	

		int i;
		int n;
		boolean ok = true;
		boolean empezo=false;
		boolean comenzar=false;
		n= input.length();
		String[] instrucciones=input.split(" ");

		i  = 0;
		try	  
		{
			while (i < n &&  ok) {
				switch (instrucciones[i]) 
				{
				case "ROBOT_R": output.append("ROBOT_R \n");comenzar=true;break;
				case "VARS" : String[] variables=instrucciones[i+1].split(","); world.inicicializarVariables(variables); output.append("VARS \n"); break;
				case "BEGIN" : output.append("BEGIN \n");empezo=true;break;

				if(empezo && comenzar)
				{
					if(instrucciones[i]=="assign:")
					{  
						int valorVariable=0;
						try{ valorVariable=Integer.parseInt(instrucciones[i+1]);}
						catch(Error e){ output.append("Se esperaba un entero 	"+e.getMessage());

						if(instrucciones[i+2]=="to")
						{
							if(world.esVariable(instrucciones[i+3]))
								world.asignarVariable(valorVariable,instrucciones[i+3]);
						}
						output.append("Se esperaba un entero");
						break;
						}
					}
					else if(instrucciones[i]=="move")
					{
						int valorVariable=0;
						try{ valorVariable=Integer.parseInt(instrucciones[i+1]);}
						catch(Error e){ output.append("Se esperaba un entero 	"+e.getMessage());}
						switch(instrucciones[i+2])
						{
						case "toThe:" : 
							switch(instrucciones[i+3])
						{
							case "left": world.girarIzquierda();
							case "right": world.girarDerecha();
							case "back": world.girarAOpuesto();
							default: output.append(" Unrecognized command:  "+ input.charAt(i)); ok=false;
						};
							
						case "inDir:":
							switch(instrucciones[i+3])
							{
								case "west": world.girarIzquierda();
								case "east": world.girarDerecha();
								case "north": world.girarANorte();
								case "south": world.girarASur();
								default: output.append(" Unrecognized command:  "+ input.charAt(i)); ok=false;
							};
						}
						world.moveForward(valorVariable);
					}
					else if(instrucciones[i]=="turn")
					{
						switch(instrucciones[i+1])
						{
						case "left": world.girarIzquierda();
						case "right": world.girarDerecha();
						case "around": world.giraraOpuesto();
						default: output.append(" Unrecognized command:  "+ input.charAt(i)); ok=false;
						}
					}
					else if(instrucciones[i]=="face")
					{
						switch(instrucciones[i+1])
						{
							case "west": world.girarIzquierda();
							case "east": world.girarDerecha();
							case "north": world.girarANorte();
							case "south": world.girarASur();
							default: output.append(" Unrecognized command:  "+ input.charAt(i)); ok=false;
						};
					}
					else if( instrucciones[i]=="put")
					{	
						int valorVariable=0;
						try{ valorVariable=Integer.parseInt(instrucciones[i+1]);}
						catch(Error e){ output.append("Se esperaba un entero "+e.getMessage());}
						if(instrucciones[i+2]=="of")
						{
							switch(instrucciones[i+3])
							{
							case "Balloons":world.putBalloons(valorVariable);
							case "Chips": world.putChips(valorVariable);
							}
						}
					}
					else if( instrucciones[i]=="pick")
					{	
						int valorVariable=0;
						try{ valorVariable=Integer.parseInt(instrucciones[i+1]);}
						catch(Error e){ output.append("Se esperaba un entero "+e.getMessage());}
						if(instrucciones[i+2]=="of")
						{
							switch(instrucciones[i+3])
							{
							case "Balloons":world.grabBalloons(valorVariable);
							case "Chips": world.pickChips(valorVariable);
							}
						}
					}
					

					else{
						output.append("expected ';' ; found end of input; ");
					}
				}

				case "END" : output.append("END \n") ; empezo=false;break;




				case "M": world.moveForward(1); output.append("move \n");break;
				case "R": world.turnRight(); output.append("turnRignt \n");break;
				case "C": world.putChips(1); output.append("putChip \n");break;
				case "B": world.putBalloons(1); output.append("putBalloon \n");break;
				case  "c": world.pickChips(1); output.append("getChip \n");break;
				case  "b": world.grabBalloons(1); output.append("getBalloon \n");break;
				default: output.append(" Unrecognized command:  "+ input.charAt(i)); ok=false;
				}

				if (ok)
				{
					if  (i+1 == n)  
					{
						output.append("expected ';' ; found end of input; ");  ok = false ;
					}
					else {
						output.append(" Expecting ;  found: "+ input.charAt(i+1)); ok=false;
					}
				}
			}

		}
		catch (Error e )
		{
			output.append("Error!!!  "+e.getMessage());
		}
		return output.toString();
	}



}
