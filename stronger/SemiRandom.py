import sys, random

def get_goodness(r,d):
    if len(d)==0:
        return 0
    return sum(v/(r-k+5) for k,v in d.items())/sum(1/(r-k+5) for k,v in d.items())

def get_att_chance(r,ad,dd,b):
    ag=get_goodness(r,ad)+500+p/2/len(b)
    dg=get_goodness(r,dd)+500    
    return ag/(ag+dg)

args=sys.argv
if len(args)==1:
    print("ok")
    with open('state/semirandom.txt','w') as f:
        f.write('-1\n1000\n10\n{}\n{}\n"S"\n')
    sys.exit()
me=int(args[1])
b=[]
for ae in args[2:]:
    if ae[-1] in 'DX':    
        ae=ae[:-1]+'-1'
    ae=ae.split(',')
    if int(ae[0])!=me:
        b+=[[int(ae[i]) for i in range(4)]]
    else:
        l,p=int(ae[1]),int(ae[2])
with open('state/semirandom.txt','r') as f:
    data=f.read()
co,lo,po,ad,dd,h=map(eval,data.split('\n')[:-1])
r=len(ad)+len(dd)
vc=l*p-lo*po
if co==0:
    ad[r]=vc
if co==1:
    dd[r]=vc   
ll=sum([be[1] for be in b])
em=ll/p/len(b)/(1000-r)*2
target_id=max(b,key=lambda be:be[1]*be[2])[0]
if random.random()<em:
    action=0
else:
    if random.random()<get_att_chance(r,ad,dd,b):
        action=0
    else:
        action=1        
if action==0:
    act=str(target_id)
else:
    act='D'
with open('state/semirandom.txt','w') as f:
    f.write('{}\n{}\n{}\n{}\n{}\n"{}"\n'.format(action,l,p,ad,dd,h+act))        
print(act)
