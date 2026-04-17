import json
from pathlib import Path
from dotenv import load_dotenv

from openai import OpenAI

load_dotenv()

MODEL = "gpt-4.1"

SCHEMA = {
    "name": "api_test_cases",
    "schema": {
        "type": "object",
        "properties": {
            "cases": {
                "type": "array",
                "items": {
                    "type": "object",
                    "properties": {
                        "name": {"type": "string"},
                        "path": {"type": "string"},
                        "expected_status": {"type": "integer"},
                        "expected_text": {"type": ["string", "null"]}
                    },
                    "required": ["name", "path", "expected_status", "expected_text"],
                    "additionalProperties": False
                }
            }
        },
        "required": ["cases"],
        "additionalProperties": False
    },
    "strict": True
}


def build_prompt() -> str:
    return """
You are a senior QA engineer.

Generate API smoke and negative test cases for a public demo website.

Base assumptions:
- Base URL points to Example Domain site
- We need simple GET cases only for now
- Return 3 test cases:
  1. Homepage returns 200
  2. Homepage contains 'Example Domain'
  3. Nonexistent page returns 404

Output must match the provided JSON schema exactly.
""".strip()


def main() -> None:
    client = OpenAI()

    response = client.responses.create(
        model=MODEL,
        input=build_prompt(),
        text={
            "format": {
                "type": "json_schema",
                "name": SCHEMA["name"],
                "schema": SCHEMA["schema"],
                "strict": SCHEMA["strict"],
            }
        },
    )

    content = response.output_text
    parsed = json.loads(content)

    cases = parsed["cases"]

    output_path = Path("tests/api/generated_cases.json")
    output_path.parent.mkdir(parents=True, exist_ok=True)

    with output_path.open("w", encoding="utf-8") as f:
        json.dump(cases, f, ensure_ascii=False, indent=2)

    print(f"Generated {len(cases)} test cases into {output_path}")


if __name__ == "__main__":
    main()