use std::io::BufRead;
use std::collections::HashMap;

fn main() -> std::io::Result<()> {
	let mut line = String::new();

	let stdin = std::io::stdin();
	let mut source = stdin.lock();

	source.read_line(&mut line)?;
	let runs = line.trim().parse::<u32>().unwrap();

	let mut database = HashMap::new();
	for _ in 0..runs {
		line.clear();
		source.read_line(&mut line)?;

		let name = line.trim();
		match database.get_mut(name) {
			Some(count) => {
				*count += 1;
				println!("{}{}", name, count);
			},
			None => {
				database.insert(name.to_owned(), 0u32);
				println!("OK");
			}
		}
	}

	Ok(())
}
