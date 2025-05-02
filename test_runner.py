#!/usr/bin/env python3

import subprocess
import re
import sys
import argparse
from typing import Dict, List, Tuple
from dataclasses import dataclass
from enum import Enum
import os
from collections import defaultdict

class TestStatus(Enum):
    PASSED = "PASSED"
    FAILED = "FAILED"
    ERROR = "ERROR"
    NOT_RUN = "NOT_RUN"

@dataclass
class TestResult:
    name: str
    status: TestStatus
    error_message: str = ""

class TestLayer:
    REPOSITORY = "repository"
    SERVICE = "service"
    CONTROLLER = "controller"
    INTEGRATION = "integration"
    ALL = "all"

class TestRunner:
    def __init__(self):
        self.test_results: Dict[str, List[TestResult]] = defaultdict(list)
        self.stats = {
            "passed": 0,
            "failed": 0,
            "error": 0,
            "not_run": 0
        }

    def run_tests(self, layer: str = TestLayer.ALL) -> None:
        """Run tests for specified layer or all layers"""
        if not os.path.exists("pom.xml"):
            print("Error: pom.xml not found. Please run from the Java project root directory.")
            sys.exit(1)

        test_pattern = self._get_test_pattern(layer)
        cmd = ["mvn", "test"]
        if layer != TestLayer.ALL:
            cmd.extend(["-Dtest=" + test_pattern])

        try:
            result = subprocess.run(cmd, capture_output=True, text=True)
            self._parse_test_output(result.stdout + result.stderr)
            self._print_results()
        except subprocess.CalledProcessError as e:
            print(f"Error running tests: {e}")
            sys.exit(1)

    def _get_test_pattern(self, layer: str) -> str:
        """Get the test pattern for the specified layer"""
        patterns = {
            TestLayer.REPOSITORY: "*RepositoryTest",
            TestLayer.SERVICE: "*ServiceTest",
            TestLayer.CONTROLLER: "*ControllerTest",
            TestLayer.INTEGRATION: "*IntegrationTest"
        }
        return patterns.get(layer, "*Test")

    def _parse_test_output(self, output: str) -> None:
        """Parse Maven test output and collect results"""
        # Reset stats
        self.stats = {"passed": 0, "failed": 0, "error": 0, "not_run": 0}
        self.test_results.clear()

        # Regular expressions for test results
        test_start = re.compile(r"Running ([\w\.]+)")
        test_result = re.compile(r"Tests run: (\d+), Failures: (\d+), Errors: (\d+), Skipped: (\d+)")
        error_details = re.compile(r"([\w\.]+).*?(?:FAILURE|ERROR)\s*!\s*(.*?)(?=\[INFO\]|\Z)", re.DOTALL)

        current_class = None
        for line in output.split('\n'):
            if match := test_start.search(line):
                current_class = match.group(1)
            
            if current_class and (match := test_result.search(line)):
                runs, failures, errors, skipped = map(int, match.groups())
                if runs == 0:
                    self.test_results[current_class].append(
                        TestResult(current_class, TestStatus.NOT_RUN)
                    )
                    self.stats["not_run"] += 1
                elif failures > 0 or errors > 0:
                    self.test_results[current_class].append(
                        TestResult(current_class, TestStatus.FAILED)
                    )
                    self.stats["failed"] += 1
                else:
                    self.test_results[current_class].append(
                        TestResult(current_class, TestStatus.PASSED)
                    )
                    self.stats["passed"] += 1

        # Parse error details
        for match in error_details.finditer(output):
            test_class, error_msg = match.groups()
            if test_class in self.test_results:
                for result in self.test_results[test_class]:
                    result.error_message = error_msg.strip()

    def _print_results(self) -> None:
        """Print test results in a formatted way"""
        print("\n=== Test Results ===\n")
        
        for layer in [TestLayer.REPOSITORY, TestLayer.SERVICE, TestLayer.CONTROLLER, TestLayer.INTEGRATION]:
            layer_tests = [(cls, results) for cls, results in self.test_results.items() 
                          if layer.upper() in cls.upper()]
            if layer_tests:
                print(f"\n{layer.upper()} LAYER:")
                print("-" * 80)
                for class_name, results in layer_tests:
                    for result in results:
                        status_color = {
                            TestStatus.PASSED: "\033[92m",  # Green
                            TestStatus.FAILED: "\033[91m",  # Red
                            TestStatus.ERROR: "\033[91m",   # Red
                            TestStatus.NOT_RUN: "\033[93m"  # Yellow
                        }
                        reset_color = "\033[0m"
                        print(f"{class_name}: {status_color[result.status]}{result.status.value}{reset_color}")
                        if result.error_message:
                            print(f"  Error: {result.error_message}")

        print("\n=== Summary ===")
        print(f"Passed: {self.stats['passed']}")
        print(f"Failed: {self.stats['failed']}")
        print(f"Errors: {self.stats['error']}")
        print(f"Not Run: {self.stats['not_run']}")
        print("=" * 80)

def main():
    parser = argparse.ArgumentParser(description="Run Java tests by layer")
    parser.add_argument("--layer", choices=["repository", "service", "controller", "integration", "all"],
                      default="all", help="Test layer to run")
    args = parser.parse_args()

    runner = TestRunner()
    runner.run_tests(args.layer)

if __name__ == "__main__":
    main() 