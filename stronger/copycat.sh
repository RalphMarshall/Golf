#!/bin/bash

if [[ "$1" == "" ]]; then echo "ok"; exit; fi

debug() {
    #echo "$(date): $*" >> copycat.log 
    return;
}

me=$1; shift
meAsTarget=0
myAction="D" #better than an invalid command
topBot=-1
topBotAwe=0
worstBot=-1
worstBotAwe=100
aliveBots=0

myMostWeakAttacker=-1
mmwaAwe=0

if [[ -e "./state/copycat.state" ]]; then
    . ./state/copycat.state
fi

for rawBot 
do
    if [[ "$rawBot" == "" ]]; then continue; fi
    if [[ $(echo $rawBot | grep -Fo ',' | wc -l) -ne 3 ]]; then continue; fi

    bot=(${rawBot//,/ })
    id=${bot[0]}; life=${bot[1]}; power=${bot[2]}; lastAction=${bot[3]}

    printf "%d\n" "$lastAction" > /dev/null 2>&1
    if [[ "$?" -ne 0 && "$lastAction" != "D" ]]; then continue; fi

    if [[ "$life" -le 0 ]]; then continue; fi
    ((aliveBots++))

    awesomeness=$(( 2 * (((life/10) * power) / (life/10 + power)) ))
    if [[ "$id" -eq "$me" ]]; then
        myLastAction="$lastAction"
        myAwe="$awesomeness"
    else
        lastBot=$id
    fi

    if [[ "$awesomeness" -gt "$topBotAwe"
        && "$lastAction" != "D"
        && "$lastAction" != "$me" ]]; then
            topBot=$id
            topBotAwe=$awesomeness
            topBotTarget=$lastAction
    fi

    if [[ "$awesomeness" -lt "$worstBotAwe" ]]; then
        worstBot=$id
        worstBotAwe=$awesomeness
    fi

    if [[ "$lastAction" -eq "$me" ]]; then
        ((meAsTarget++))
        if [[ "$awesomeness" -lt "$mmwaAwe" ]]; then
            myMostWeakAttacker=$id
            mmwaAwe=$awesomeness
        fi
    fi
done

backupStrategy() {
    if [[ "$myMostWeakAttacker" != "-1" && "$mmwaAwe" -lt "$myAwe" ]]; then
        debug "attacking my most weak attacker ($myMostWeakAttacker) who is weaker then me"
        myAction=$myMostWeakAttacker
    elif [[ "$worstBot" != "-1" && "$worstBot" != "$me" ]]; then
        debug "attacking the worst bot $worstBot"
        myAction=$worstBot
    elif [[ "$myMostWeakAttacker" != "-1" ]]; then
        debug "attacking my most weak attacker $myMostWeakAttacker"
        myAction=$myMostWeakAttacker
    else
        debug "no one is attacking me anymore; attacking the last ones"
        myAction=$lastBot
    fi
}

if [[ "$meAsTarget" -gt "$((aliveBots/2))" ]]; then
    #hit-and-run
    if [[ "$myLastAction" == "D" && "$myAwe" -gt "$worstBotAwe" ]]; then
        debug "I am still under fire, but not the worst one.."
        backupStrategy
    else
        debug "I was attacked to much; defending now (attack level: $meAsTarget)"
        myAction="D"
        meAsTarget=$((meAsTarget-aliveBots/2))
    fi
elif [[ "$topBotTarget" != "" ]]; then
    myAction=$topBotTarget

    for rawBot
    do
        if [[ "$rawBot" == "" ]]; then break; fi
        bot=(${rawBot//,/ })
        if [[ "${bot[0]}" -eq "$topBotTarget" ]]; then
            if [[ "${bot[1]}" -le 0 ]]; then
                backupStrategy
            else
                debug "copying strategy from bot $topBot attacking $myAction"
            fi
            break
        fi
    done
else
    backupStrategy
fi

if ! [[ -d "./state" ]]; then mkdir -p "./state"; fi
cat <<EOF_STATE > "./state/copycat.state"
topBotTarget="$topBotTarget"
meAsTarget="$meAsTarget"
EOF_STATE

echo "$myAction"
exit 0
