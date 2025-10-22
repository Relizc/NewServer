import os

def count_java_lines(root_dir="."):
    total_lines = 0
    file_count = 0

    for dirpath, dirnames, filenames in os.walk(root_dir):
        # Only consider folders that include src/main/java
        if "src/main/java" in dirpath.replace("\\", "/"):
            
            for filename in filenames:
                print(filename)
                if filename.endswith(".java"):
                    file_path = os.path.join(dirpath, filename)
                    try:
                        with open(file_path, encoding="utf-8", errors="ignore") as f:
                            lines = sum(1 for _ in f)
                            total_lines += lines
                            file_count += 1
                    except Exception as e:
                        print(f"Could not read {file_path}: {e}")

    return file_count, total_lines


if __name__ == "__main__":
    directory = "."  # change to your project root if needed
    files, lines = count_java_lines(directory)
    print(f"Found {files} Java files under src/main/java folders.")
    print(f"Total lines of code: {lines}")
