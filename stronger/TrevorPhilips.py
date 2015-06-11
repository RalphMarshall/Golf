# "Computer gamers with their computer rigs. 
#  All they do is cry. 
#  They will never get the game."
#                         - Trevor Phillips.
#
import sys, random
from __builtin__ import str

# YES!!! I'm alive!!!
args=sys.argv
if len(args)==1:
    print("ok")
    sys.exit()

# Basic strategy: Hit whoever hits me or just anyone; sometimes brag a Trevor quote.
me=int(args[1])
botnrs=[]
action=''
quotes=["Now would you get me a f*cking drink! I'm not gonna ask you again!",
        "Grumble grumble grumble I've got my work grumble grumble grumble I've got my life, never the two shall meet.",
        "Well hello there beautiful. here go buy yourself something nice",
        "I'm driving, you can jerk me off if I get bored... I'm joking, you can suck me off.",
        "Do you want me to get my dick out again?!",
        "Floyd..we're having people over.  We need chips, dip, and prostitutes"]
for bot in args[2:]:
    nr,life,power,lastaction=bot.split(',')
    botnrs.append(nr)
    if lastaction==str(me):
        action=int(nr)
if action=='':
    action=int(random.choice(botnrs))
if random.random()<0.05:
    action=random.choice(quotes)
print(action)
