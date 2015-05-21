#!/usr/bin/perl

use feature qw(say);
use POSIX;

my @ops = ('+', '-', '*', '/', '');

# Multiplication and division by 1 are not interesting
my @oneOps = ('+', '-', '');

# Multiplication and division by zero are not helpful, and
# addition and subtraction of a 0 is not interesting, so we just concatenate
my @zeroOps = ('');

sub checkAnswer ($$) {

    my ($target, $candidate) = @_;

    my $actual = eval $candidate;
    my $retval = abs($target-$actual);
    return $retval;
}

sub randomIndex(@) {

    return floor(rand(@_));
}

sub pickFromList(@) {

    my @source = @_;

    my $chosenIndex = randomIndex(@source);
    my $retval = $source[$chosenIndex];

    return $retval;
}

sub dumpState(%) {

    my (%state) = @_;

    for (sort {$a <=> $b } keys %state) {
        say "Score $_ for candidate $state{$_}";
    }
}

sub pickTopNCandidates($%) {

    my ($n, %state) = @_;

    my $i = 0;
    my @retval;

    map { push @retval, $state{$_} if $i++ < $n } sort {$a <=> $b } keys %state;

    return @retval;
}

sub pickBest (%) {

    my %state = @_;

    my @best = pickTopNCandidates(1, %state);
    return $best[0] || undef;
}

sub seedSolution($@) {

    my ($target, @inputs) = @_;

    my %retval;

    for my $candidateCounter (1..10000) {
        my $numInputs = randomIndex(@inputs);

        my @localInputs = @inputs;
        my $candidate;
        for my $inputCounter (0..$numInputs) {

            # Pick without replacement as inputs cannot be reused
            my $nextIndex = randomIndex(@localInputs);
            my $nextInput = $localInputs[$nextIndex];
            splice @localInputs, $nextIndex, 1;

            if (length $candidate) {
                my @localOps = $nextInput == 1 ? @oneOps : $nextInput == 0 ? @zeroOps : @ops;
                $candidate .= pickFromList(@localOps) . $nextInput;
            } else {
                $candidate = $nextInput;
            }
        }

        $retval{checkAnswer($target, $candidate)} = $candidate;
    }

    # dumpState(%retval);
    return %retval;
}

sub findSolution($@) {
    my ($target, @inputs) = @_;

    my %startingSet = seedSolution($target, @inputs);

    my $bestExpression = pickBest(%startingSet);
    my $bestEval = eval $bestExpression;
    my $bestScore = checkAnswer($target, $bestExpression);
    say "Best answer for $target is $bestExpression [actual value = $bestEval, score = $bestScore], out of ", scalar(keys %startingSet), " unique candidates";
}

while (<DATA>) {
    next if /^#/;
    chomp;
    y/ //d;
    my ($input, @remainder) = split(',');
    next unless @remainder;
    # say "Make $input from ", join(' | ', @remainder);

    findSolution($input, @remainder);
}

__END__
14142, 10, 11, 12, 13, 14, 15
48077691, 6, 9, 66, 69, 666, 669, 696, 699, 966, 969, 996, 999
333723173, 3, 3, 3, 33, 333, 3333, 33333, 333333, 3333333, 33333333, 333333333
589637567, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5
8067171096, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199
78649377055, 0, 2, 6, 12, 20, 30, 42, 56, 72, 90, 110, 132, 156, 182, 210, 240, 272, 306, 342, 380, 420, 462, 506, 552, 600, 650, 702, 756, 812, 870, 930, 992
792787123866, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025, 121393, 196418, 317811, 514229, 832040, 1346269, 2178309, 3524578, 5702887, 9227465, 14930352, 24157817, 39088169
2423473942768, 1, 2, 5, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 20000, 50000, 100000, 2000000, 5000000, 10000000, 20000000, 50000000

