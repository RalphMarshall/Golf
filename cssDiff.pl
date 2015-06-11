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
        $wholeFile = doPrune($wholeFile, 's/::?(-|\w)+(\(.*?\))?//g', 'Pseudo classes');
        $wholeFile = doPrune($wholeFile, 's/\@media.*?(?={)//g', 'Media queries');
        $wholeFile = doPrune($wholeFile, 's/{.*?}/ /g', 'CSS Body');               # Remove body of the CSS - all we want is the selectors
        $wholeFile = doPrune($wholeFile, 's/\[.*?\]//g', 'Attribute selector');    # Remove attribute qualifications from selectors
        $wholeFile = doPrune($wholeFile, 's/(\S)[.]/$1 ./g');                      # Separate out all selectors that look like ".one.two" into ".one .two"

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

sub reportSubset($@) {
    my ($label, @classes) = @_;

    say $OUT "\n################################################################";
    say $OUT "CSS Classes present $label";
    say $OUT "################################################################";
    say $OUT join("\n", @classes);
}

sub reportChanges($$$) {
    my ($oldRef, $newRef, $verbose) = @_;
    my @oldClasses = @$oldRef;
    my @newClasses = @$newRef;

    # Separate out into classes only in the old CSS, those only in the new CSS,
    # and those common to both.
    my (@onlyOld, @onlyNew, @common);

    # Lists are already sorted, so just do a basic merge
    while (@oldClasses || @newClasses) {

        my $nextToken;

        print $LOG "Old head is ", @oldClasses ? $oldClasses[0] : "empty",
        ", New head is ", @newClasses ? $newClasses[0] : "empty",
        " so ";

        if (!@oldClasses) {
            # Only have new classes left
            push @onlyNew, @newClasses;
            print $LOG join(", ", @newClasses), " all added to new classes\n";
        } elsif (!@newClasses) {
            # Have only old classes left
            push @onlyOld, @oldClasses;
            print $LOG join(", ", @oldClasses), " all added to old classes\n";
        } elsif ($oldClasses[0] eq $newClasses[0]) {
            # Matching case
            $nextToken = shift @oldClasses;
            shift @newClasses;
            push @common, $nextToken;
            print $LOG "$nextToken added to common\n";
        } elsif ($oldClasses[0] lt $newClasses[0]) {
            # Found an old class not in the new classes
            $nextToken = shift @oldClasses;
            push @onlyOld, $nextToken;
            print $LOG "$nextToken added to old\n"
        } else {
            # Found a new class not in the old classes
            $nextToken = shift @newClasses;
            push @onlyNew, $nextToken;
            print $LOG "$nextToken added to new\n";
        }
    }

    say $OUT "------------------- Start of report -------------------";
    reportSubset('only in the old file', @onlyOld);
    reportSubset('only in the new file', @onlyNew);
    reportSubset('in both files', @common) if $verbose;
    say $OUT "-------------------- End of report --------------------";
}

 MAIN: {
     my ($oldDir, $newDir, $flags) = @ARGV;

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

     reportChanges(\@rawOldClasses, \@rawNewClasses, $flags && $flags =~ /-v|--verbose/);
}
