import sys
class Berserker:
    def rage(self,command):


        if len(command) <=1:
            return 'ok'
        else:
            last_turn = list(command)
            bot_to_smash = -1
            me = last_turn[0]
            my_power = last_turn[int(me)].split(',')[2]
            for k in xrange(0,len(last_turn)):
                #bot_action = last_turn.split(',')

                if len(last_turn[k]) ==1:
                    pass
                else:
                    bot_action = last_turn[k].split(',')
                    if int(my_power) < 25:
                        #Too weak! Need make stronger for smashing!
                        bot_to_smash = me
                        break
                    else:
                        #Now strong! Smash! Not smash broken things!
                        if bot_action[0] != me and bot_action[1] > 0:
                            bot_to_smash = bot_action[0]

            if bot_to_smash > -1:
                return bot_to_smash
            else:
                #Confused! Don't like! MORE POWER!
                return me

print Berserker().rage(sys.argv[1:])
