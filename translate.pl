    #!/usr/bin/perl
    
    while (<>) {
        print map { 
            s/^([bcdfghjklmnpqrstvwxyz]*)([a-z]+)/$2$1ay/i if /[a-z][a-z]/i; $_ 
        } split(/\b/);
    }
    __END__
    # listen (a perl poem)
    # Sharon Hopkins
    # rev. June 19, 1995
    # Found in the "Perl Poetry" section of the Camel book
    APPEAL:
    
    listen(please, please);
    
    open yourself, wide;
        join (you, me),
    connect (us, together),
    
    tell me.
    
    do something if distressed;
    
        @dawn, dance;
        @evening, sing;
        read (books, $poems, stories) until peaceful;
        study if able;
    
        write me if-you-please;
    
    sort your feelings, reset goals, seek (friends, family, anyone);
    
        do*not*die (like this)
        if sin abounds;
    
    keys (hidden), open (locks, doors), tell secrets;
    do not, I-beg-you, close them, yet.
    
            accept (yourself, changes),
            bind (grief, despair);
    
    require truth, goodness if-you-will, each moment;
    
    select (always), length (of-days)

