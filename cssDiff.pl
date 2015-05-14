#!/usr/bin/perl

use strict;
use warnings;

use feature qw(say);
use List::Util qw(pairmap);
use Data::Dumper;

sub usage($) {
    my ($errMsg) = @_;

    say "ERROR: $errMsg";
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
        say "Total of $#files files for $label";
        say join("\n", @files);
    } (
        "Version 1.22 ($oldDir)" => \@oldFiles,
        "Version 1.28 ($newDir)" => \@newFiles
    );
}

sub extractClasses(@) {
    my (@allFiles) = @_;

    my @retval=();

    for my $nextFile (@allFiles) {

        local $/ = undef; # Read in entire file as one string
        open FILE, "$nextFile" || usage("Failed to read $nextFile: $!");

        my $wholeFile = <FILE>;
        $wholeFile =~ tr/\n\r,>+/ /;      # Convert newlines and useless punctuation to whitespace

        $wholeFile =~ s@(/\*.*?\*/)@@g;  # Remove comments
        $wholeFile =~ s/\@import.*?;//g; # Remove css import statements
        $wholeFile =~ s/{.*?}/ /g;       # Remove body of the CSS - all we want is the selectors
        $wholeFile =~ s/\[.*?\]//g;      # Remove attribute qualifications from selectors

        $wholeFile =~ s/(\S)[.]/$1 ./g;  # Separate out all selectors that look like ".one.two" into ".one .two"

        my @allClasses = split(/\s+/, $wholeFile);
        map { s/:.*// } @allClasses;     # Remove any pseudo classes

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

     say "Old root directory: $oldDir";
     say "New root directory: $newDir";

     @oldFiles = findFileNames($oldDir);
     @newFiles = findFileNames($newDir);

     reportNames($oldDir, \@oldFiles, $newDir, \@newFiles);

     @rawOldClasses = extractClasses(@oldFiles);
     @rawNewClasses = extractClasses(@newFiles);

     say "List of unique selectors across all old files";
     for (@rawOldClasses) {
         say $_;
     }
}
