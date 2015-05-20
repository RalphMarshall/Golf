import json, sys
from random import choice

#'S'/'W' = attack high/low power (strong/weak)
#'H'/'F'  = attack high/low health (hale/frail)
#'A' = attack defender (armor)
#'R' = attack random (it doesn't know)
#'D' = defend

amin = lambda x: x.index(min(x))
amax = lambda x: x.index(max(x))

def pick(history, ids, action):
    if action == 'D':
        return 'D'
    if action == 'R' or len(history['all'][-1][action]) < 1:
        return choice(ids)
    return choice(history['all'][-1][action])

args = sys.argv
if len(args) == 1:
    print 'ok'
    sys.exit()
me = args[1]

def notme(l):
    tmp = list(l)
    try:
        tmp.remove(me)
    except ValueError:
        pass
    return tuple(tmp)

args = args[2:]
try:
    with open('precog.state') as f:
        history = json.load(f)
except (IOError, ValueError):
    history = {}
if len(history) == 0:
    history = {'all':[]}

args = [a.split(',') for a in args]
ids,hps,pows,acts = zip(*args)
hps,pows = map(int,hps), map(int,pows)

for i,h,p,a in args:
    if a == 'X': #most people will try not to attack armored
        history[i] = {'a':'SWHFRD','health':[],'power':[],'score':[]}
    elif acts == 'D':
        history[i]['a'] += 'D'
    else:
        for x in 'SWHFA':
            if a in history['all'][-1][x]:
                history[i]['a'] += x
                break
        else:
            history[i]['a'] += 'R'
    history[i]['health'] += int(h),
    history[i]['power'] += int(p),
    history[i]['score'] += int(h)*int(p),

history['all'] += {'S':[ids[amax(pows)]],
                   'W':[ids[amin(pows)]],
                   'H':[ids[amax(hps)]],
                   'F':[ids[amin(hps)]],
                   'A':[ids[i] for i in filter(lambda x:acts[x]=='D',range(len(acts)))]},

with open('precog.state','w') as f:
    json.dump(history,f)

scores = dict(zip('SWHFRAD',[0]*7))
for _ in range(50):
    for act in 'SWHFRAD':
        _score = {}
        p,h,a = dict(zip(ids,pows)),dict(zip(ids,hps)),{i:0 for i in ids}
        opp = {i:choice(history[i]['a']) for i in ids if i != me}
        opp[me] = act
        m = {o:[1,2][opp[o]=='D'] for o in opp}
        for o in opp:
            if opp[o] != 'D':
                if o == me:
                    target = pick(history, notme(ids), opp[o])
                else:
                    target = pick(history, ids, opp[o])
                h[target] -= p[o]/m[target]
                a[target] += 1
        for o in opp:
            p[o] += m[o] * a[o]
            _score[o] = p[o] * h[o]
        scores[act] += _score.pop(me) - sum(_score.values())

target = pick(history, notme(ids), scores.keys()[amax(scores.values())])
if target == me:
    target = choice(notme(ids))
print target
