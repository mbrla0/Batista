use std::ops::Range;

fn main() {
	let input = std::io::stdin();
	let mut input = Scanner::new(input.lock());

	let pairs = input.next::<u16>();
	for _ in 0..pairs {
		let template = input.next::<String>();
		let subject = input.next::<String>();

		let template = template.chars().collect::<Vec<_>>();
		let template = CharClusterScanner::new(&template[..]);

		let expected = subject.chars().count();
		let mut subject = subject.chars().peekable();

		/* Every occurrence of a character in the template must imply in at
		 * least one occurrence of that same character in the subject. */
		let mut failed = false;
		let mut consumed = 0usize;
		for cluster in template {
			let char = cluster[0];
			let min = cluster.len();

			let mut matches= 0usize;
			while let Some(candidate) = subject.peek() {
				if *candidate == char {
					matches += 1;
					let _ = subject.next();
				} else { break }
			}

			if matches < min {
				/* Reject the subject. */
				failed = true;
				break
			} else {
				consumed += matches;
			}
		}

		if consumed == expected && !failed {
			println!("YES");
		} else {
			println!("NO");
		}
	}
}

struct CharClusterScanner<'a> {
	chars: &'a [char],
	cursor: usize,
	buffer: Range<usize>,
	flush: bool,
}
impl<'a> CharClusterScanner<'a> {
	pub fn new(chars: &'a [char]) -> Self {
		Self {
			chars,
			cursor: 0,
			buffer: 0..0,
			flush: false
		}
	}
}
impl<'a> Iterator for CharClusterScanner<'a> {
	type Item = &'a [char];
	fn next(&mut self) -> Option<Self::Item> {
		if self.flush {
			self.buffer = self.cursor..self.cursor;
			self.flush = false;
		}

		macro_rules! next {
			($self:expr) => {
				if self.cursor < self.chars.len() {
					self.cursor += 1;
					Some(self.chars[ self.cursor - 1 ])
				} else { None }
			}
		}

		loop {
			if self.buffer.len() == 0 {
				match next!(self) {
					Some(_) => self.buffer.end += 1,
					None => return None
				};
				continue
			}

			if self.cursor >= self.chars.len() {
				return if self.buffer.len() > 0 {
					self.flush = true;
					Some(&self.chars[self.buffer.clone()])
				} else {
					None
				}
			} else {
				let candidate = self.chars[self.cursor];
				if candidate == self.chars[self.buffer.start] {
					self.buffer.end += 1;
					let _ = next!(self);
				} else {
					self.flush = true;
					return Some(&self.chars[self.buffer.clone()])
				}
			}
		}
	}
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