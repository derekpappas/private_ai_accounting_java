#!/usr/bin/env python3

import os
import sys
import subprocess
import venv
import argparse
from typing import Dict, List, Tuple
from dataclasses import dataclass
from enum import Enum
import re
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
    layer: str
    error_message: str = ""

class TestStats:
    def __init__(self):
        self.total = 0
        self.passed = 0
        self.failed = 0
        self.errors = 0
        self.not_run = 0

    def update(self, status: TestStatus):
        self.total += 1
        if status == TestStatus.PASSED:
            self.passed += 1
        elif status == TestStatus.FAILED:
            self.failed += 1
        elif status == TestStatus.ERROR:
            self.errors += 1
        else:
            self.not_run += 1

    def __str__(self):
        return (f"Total: {self.total}, Passed: {self.passed}, "
                f"Failed: {self.failed}, Errors: {self.errors}, "
                f"Not Run: {self.not_run}")

class TestEnvironment:
    def __init__(self):
        self.venv_dir = "venv"
        self.requirements_file = "requirements.txt"
        self.test_runner = "test_runner.py"
        self.results: List[TestResult] = []
        self.stats_by_layer: Dict[str, TestStats] = defaultdict(TestStats)
        self.overall_stats = TestStats()

    def ensure_venv(self):
        """Ensure virtual environment exists and is set up correctly"""
        if not os.path.exists(self.venv_dir):
            print("Creating virtual environment...")
            venv.create(self.venv_dir, with_pip=True)
            self.install_requirements()
        return self.get_python_path()

    def get_python_path(self):
        """Get the Python interpreter path from the virtual environment"""
        if sys.platform == "win32":
            return os.path.join(self.venv_dir, "Scripts", "python.exe")
        return os.path.join(self.venv_dir, "bin", "python")

    def install_requirements(self):
        """Install required packages from requirements.txt"""
        pip_path = os.path.join(os.path.dirname(self.get_python_path()), "pip")
        if os.path.exists(self.requirements_file):
            print("Installing requirements...")
            subprocess.run([pip_path, "install", "-r", self.requirements_file], check=True)
        else:
            print(f"Warning: {self.requirements_file} not found")

    def get_test_layer(self, test_name: str) -> str:
        """Determine the test layer from the test name"""
        if "RepositoryTest" in test_name:
            return "Repository"
        elif "ServiceTest" in test_name:
            return "Service"
        elif "ControllerTest" in test_name:
            return "Controller"
        elif "IntegrationTest" in test_name:
            return "Integration"
        return "Other"

    def parse_test_output(self, output: str, target_layer: str = None):
        """Parse Maven test output and collect results"""
        self.results.clear()
        self.stats_by_layer.clear()
        self.overall_stats = TestStats()

        test_start = re.compile(r"Running ([\w\.]+)")
        test_result = re.compile(r"Tests run: (\d+), Failures: (\d+), Errors: (\d+), Skipped: (\d+)")
        error_details = re.compile(r"([\w\.]+).*?(?:FAILURE|ERROR)\s*!\s*(.*?)(?=\[INFO\]|\Z)", re.DOTALL)

        current_class = None
        error_messages = {}

        # First pass: collect error messages
        for match in error_details.finditer(output):
            test_class, error_msg = match.groups()
            error_messages[test_class] = error_msg.strip()

        # Second pass: collect test results
        for line in output.split('\n'):
            if match := test_start.search(line):
                current_class = match.group(1)
                continue

            if current_class and (match := test_result.search(line)):
                runs, failures, errors, skipped = map(int, match.groups())
                layer = self.get_test_layer(current_class)

                if target_layer and layer.lower() != target_layer.lower():
                    continue

                if runs == 0:
                    status = TestStatus.NOT_RUN
                elif failures > 0 or errors > 0:
                    status = TestStatus.FAILED
                else:
                    status = TestStatus.PASSED

                result = TestResult(
                    name=current_class,
                    status=status,
                    layer=layer,
                    error_message=error_messages.get(current_class, "")
                )
                self.results.append(result)
                self.stats_by_layer[layer].update(status)
                self.overall_stats.update(status)

    def print_results(self, target_layer: str = None):
        """Print test results in a formatted way"""
        print("\n=== Test Results ===\n")

        # Sort results by layer
        results_by_layer = defaultdict(list)
        for result in self.results:
            if not target_layer or result.layer.lower() == target_layer.lower():
                results_by_layer[result.layer].append(result)

        # Print results for each layer
        for layer in sorted(results_by_layer.keys()):
            print(f"\n{layer.upper()} LAYER:")
            print("-" * 80)
            
            for result in sorted(results_by_layer[layer], key=lambda x: x.name):
                status_color = {
                    TestStatus.PASSED: "\033[92m",  # Green
                    TestStatus.FAILED: "\033[91m",  # Red
                    TestStatus.ERROR: "\033[91m",   # Red
                    TestStatus.NOT_RUN: "\033[93m"  # Yellow
                }
                reset_color = "\033[0m"
                print(f"{result.name}: {status_color[result.status]}{result.status.value}{reset_color}")
                if result.error_message:
                    print(f"  Error: {result.error_message}")
            
            # Print layer statistics
            print(f"\n{layer} Statistics:")
            print(f"  {self.stats_by_layer[layer]}")

        # Print overall statistics
        print("\n=== Overall Statistics ===")
        print(self.overall_stats)
        print("=" * 80)

    def run_tests(self, layer: str = None):
        """Run the test runner with provided arguments"""
        python_path = self.ensure_venv()
        
        # Prepare Maven command
        mvn_cmd = ["mvn", "test"]
        if layer and layer.lower() != "all":
            test_pattern = f"**/*{layer.capitalize()}Test.java"
            mvn_cmd.extend([f"-Dtest={test_pattern}"])

        try:
            result = subprocess.run(mvn_cmd, capture_output=True, text=True)
            self.parse_test_output(result.stdout + result.stderr, layer if layer != "all" else None)
            self.print_results(layer if layer != "all" else None)
        except subprocess.CalledProcessError as e:
            print(f"Error running tests: {e}")
            sys.exit(1)

def main():
    parser = argparse.ArgumentParser(description="Run Java tests with detailed statistics")
    parser.add_argument("--layer", choices=["repository", "service", "controller", "integration", "all"],
                      default="all", help="Test layer to run")
    args = parser.parse_args()

    env = TestEnvironment()
    env.run_tests(args.layer)

if __name__ == "__main__":
    main() 