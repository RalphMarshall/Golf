#!/usr/bin/perl

my $input="123456789";

sub transform($$) {

    my ($left, $right) = @_;

    my @newLeft;
    for $soFar (@$left) {
        push(@newLeft, map($soFar . $_ . $$right[0], '-', '+', ''));
    }

    shift(@$right);
    if (@$right) {
        return transform(\@newLeft, $right);
    } else {
        return @newLeft;
    }
}

my @digits = split('', $input);
my @results = transform([$digits[0]], [@digits[1..$#digits]]);

print join("\n", grep(eval==100, @results)), "\n";
