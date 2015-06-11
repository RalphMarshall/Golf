import sys
class snapping_turtle:
    def bitey(self,command):


        if len(command) <=1:
            return 'ok'
        else:
            last_turn = list(command)
            bot_to_bite = -1
            me = last_turn[0]
            for k in xrange(0,len(last_turn)):
                #bot_action = last_turn.split(',')

                if len(last_turn[k]) ==1:
                    pass
                else:
                    bot_action = last_turn[k].split(',')
                    # If they hit me
                    if bot_action[3] == me:
                        # And if they're still alive, hit them
                        if int(bot_action[1]) > 0:
                            bot_to_bite = bot_action[0]
                            break
                        #Otherwise, stay in my shell
                        else:
                            pass

            if bot_to_bite > -1:
                return bot_to_bite
            else:
                return 'D'
print snapping_turtle().bitey(sys.argv[1:])
