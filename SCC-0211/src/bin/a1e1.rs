use std::io::BufRead;

fn main() -> std::io::Result<()> {
	let mut line = String::new();

	let stdin = std::io::stdin();
	let mut source = stdin.lock();

	source.read_line(&mut line)?;
	let runs = u64::from_str_radix(line.trim(), 10).unwrap();

	let mut solution = Vec::new();
	let mut set = Vec::new();

	for _ in 0..runs {
		line.clear();
		source.read_line(&mut line)?;

		let count = line.trim().parse::<usize>().unwrap();

		solution.clear();
		solution.reserve(count);
		set.clear();
		set.resize(count * 2, false);

		line.clear();
		source.read_line(&mut line)?;

		let numbers = line.split_whitespace()
			.map(|number| usize::from_str_radix(number, 10).unwrap());
		for number in numbers {
			if !set[number - 1] {
				set[number - 1] = true;
				solution.push(number);
			}
		}

		for number in &solution {
			print!("{} ", number)
		}
		println!();
	}

	Ok(())
}
