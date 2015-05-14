#!/usr/bin/perl

use strict;
use warnings;

use feature qw(say);
use List::Util qw(pairmap);
use Data::Dumper;

my ($LOG, $OUT);

sub usage($) {
    my ($errMsg) = @_;

    say STDERR "ERROR: $errMsg";
    die "Exiting with extreme prejudice\n";
}

sub findFileNames($) {
    my ($rootDir) = @_;

    open FIND, "find $rootDir -name library.css -print|" || usage "Unable to run find on $rootDir: $!";

    # Ignore anything in the test-resources tree
    my @retval = grep { chomp; !m[/test-resources/] } <FIND>;

    return @retval;
}

sub shortenNames($@) {

    my ($prefix, @names) = @_;
    my @retval;

    for (@names) {
        my $copy = $_;
        $copy =~ s/$prefix/.../;
        push @retval, $copy;
    }

    return @retval;
}

sub reportNames($$$$) {
    my ($oldDir, $oldFiles, $newDir, $newFiles) = @_;

    my @oldFiles = shortenNames($oldDir, @$oldFiles);
    my @newFiles = shortenNames($newDir, @$newFiles);

    pairmap {
        my ($label, @files) = ($a, @$b);
        say $OUT "Total of $#files files for $label";
        say $OUT join("\n", @files);
    } (
        "Version 1.22 ($oldDir)" => \@oldFiles,
        "Version 1.28 ($newDir)" => \@newFiles
    );
}

sub doPrune($$;$) {
    my ($var, $pat, $label) = @_;

    my $origVar = $var;

    my $code = "\$var =~ $pat && \$&;";

    my $evalRet = eval($code);
    if ($evalRet) {
        say $LOG "[Pruned $label] $evalRet" if $label;
    }

    die if $origVar eq $var && $evalRet;

    return $var;
}

sub extractClasses(@) {
    my (@allFiles) = @_;

    my @retval=();

    for my $nextFile (@allFiles) {

        local $/ = undef; # Read in entire file as one string
        open FILE, "$nextFile" || usage("Failed to read $nextFile: $!");

        my $wholeFile = <FILE>;
        $wholeFile =~ tr/\n\r,>+/ /;      # Convert newlines and useless punctuation to whitespace

        $wholeFile = doPrune($wholeFile, 's@/\*.*?\*/@@g', 'Comments');            
        $wholeFile = doPrune($wholeFile, 's/\@import.*?;//g', 'CSS Import');       
        $wholeFile = doPrune($wholeFile, 's/\@media.*?(?={)//g', 'Media queries');
        $wholeFile = doPrune($wholeFile, 's/{.*?}/ /g', 'CSS Body');               # Remove body of the CSS - all we want is the selectors
        $wholeFile = doPrune($wholeFile, 's/\[.*?\]//g', 'Attribute selector');    # Remove attribute qualifications from selectors
        $wholeFile = doPrune($wholeFile, 's/(\S)[.]/$1 ./g');                      # Separate out all selectors that look like ".one.two" into ".one .two"
#        $wholeFile = doPrune($wholeFile, 's/:.*?(\(.*?\))??(\b|\.)//g', 'Pseudo clases');
        my @allClasses = split(/\s+/, $wholeFile);
        # map { s/:.*// } @allClasses;     # Remove any pseudo classes

        map { say $LOG "[PAREN] Found $_ in $nextFile" if /^\(/ || /\)$/} @allClasses;

        # Stuff results for this file onto our list for all files
        push @retval, @allClasses;
    }

    # Remove duplicates
    my %uniq;
    map {
        # Let the hash table collapse duplicate values for us
        $uniq{$_} = 1;
    } grep {
        # Strip out standard HTML element names and the * selector
        !m/^(\*|a|body|button|div|h[1-6]|header|hr|html|img|li|p|section|span|ul)$/i
    } @retval;

    # Finally return the sorted list of unique selectors
    return sort keys %uniq;
}

 MAIN: {
     my ($oldDir, $newDir) = @ARGV;

     my (@oldFiles, @rawOldClasses, @newFiles, @rawNewClasses);

     usage("Invalid arguments") unless $oldDir && $newDir;

     open $LOG, ">cssDiff.log" || die "Unable to write log file: $!\n";
     open $OUT, ">cssDiff.out" || die "Unable to write result file: $!\n";
     
     say $OUT "Old root directory: $oldDir";
     say $OUT "New root directory: $newDir";

     @oldFiles = findFileNames($oldDir);
     @newFiles = findFileNames($newDir);

     reportNames($oldDir, \@oldFiles, $newDir, \@newFiles);

     @rawOldClasses = extractClasses(@oldFiles);
     @rawNewClasses = extractClasses(@newFiles);

     say $OUT "List of unique selectors across all old files";
     for (@rawOldClasses) {
         say $OUT $_;
     }
}
