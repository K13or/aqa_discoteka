import json
from pathlib import Path


def load_test_cases():
    path = Path(__file__).parent / "test_cases.json"
    with open(path, "r", encoding="utf-8") as f:
        return json.load(f)