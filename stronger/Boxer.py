import sys, re, random
if sys.argv[1:]:
    rows = [map(int, re.sub('[DX]', '-1', b).split(',')) for b in sys.argv[2:]]
    bots = dict((r.pop(0),r) for r in rows if r[1]>0 and r[0]!=int(sys.argv[1]))
    target = max(bots, key=lambda b: bots[b][0]-300*(bots[b][2]==-1))
    print target if random.randint(1,100) > 70 else 'D'
else:
    print 'ok'
    
