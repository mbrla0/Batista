fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let runs = input.next::<u8>();
	for _ in 0..runs { run(&mut input); }
}

fn run<R: std::io::BufRead>(input: &mut Scanner<R>) {
	let string = input.next::<String>().chars().collect::<Vec<_>>();
	let pattern = input.next::<String>().chars().collect::<Vec<_>>();

	let mut matches = Vec::new();
	let mut i = 0usize;
	let mut j = 0usize;
	let mut b = 0usize;
	while i < string.len() {
		if string[i] == pattern[j] {
			i += 1;
			j += 1;

			if j >= pattern.len() {
				matches.push(b);
				b = i;
				j = 0;
			}
		} else {
			i += 1;
			j = 0;
			b = i;
		}
	}

	if matches.len() > 0 {
		println!("{}", matches.len());
		for offset in matches {
			print!("{} ", offset + 1);
		}
		println!()
	} else {
		println!("Not Found");
	}
	println!()
}

struct Scanner<R> { source: R, buffer: std::collections::VecDeque<String> }
impl<R: std::io::BufRead> Scanner<R> {
	fn new(source: R) -> Self {
		Self { source, buffer: Default::default() }
	}
	fn next<T: std::str::FromStr>(&mut self) -> T {
		while self.buffer.len() == 0 {
			let mut line = String::new();
			let result = std::io::BufRead::read_line(&mut self.source, &mut line);
			if result.unwrap() == 0 { break }

			line.split_whitespace()
				.map(|str| str.trim().to_owned())
				.for_each(|str| self.buffer.push_back(str));
		}
		self.buffer.pop_front()
			.and_then(|str| str.parse().ok())
			.unwrap()
	}
}