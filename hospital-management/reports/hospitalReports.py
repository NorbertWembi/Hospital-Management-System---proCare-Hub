"""
Hospital Management System - Data Reporting Script
Author: Norbert Wembi
Description:
Connects to the hospital SQL database, extracts patients, appointments,
and billing data, and generates summary reports with visualizations.
"""

import mysql.connector
import pandas as pd
import matplotlib.pyplot as plt

# -----------------------------
# Database Configuration
# -----------------------------
DB_CONFIG = {
    "host": "localhost",
    "user": "root",
    "password": "your_password",
    "database": "hospital_db"
}

# -----------------------------
# Database Connection
# -----------------------------
def connect_db():
    return mysql.connector.connect(**DB_CONFIG)

# -----------------------------
# Data Extraction
# -----------------------------
def fetch_data():
    conn = connect_db()

    patients_query = "SELECT * FROM patients"
    appointments_query = "SELECT * FROM appointments"
    billing_query = "SELECT * FROM billing"

    patients_df = pd.read_sql(patients_query, conn)
    appointments_df = pd.read_sql(appointments_query, conn)
    billing_df = pd.read_sql(billing_query, conn)

    conn.close()
    return patients_df, appointments_df, billing_df

# -----------------------------
# Reporting
# -----------------------------
def generate_reports(patients, appointments, billing):
    # Save CSV reports
    patients.to_csv("patients_report.csv", index=False)
    appointments.to_csv("appointments_report.csv", index=False)
    billing.to_csv("billing_report.csv", index=False)

    print("CSV reports generated successfully.")

# -----------------------------
# Visualizations
# -----------------------------
def generate_visualizations(appointments, billing):
    # Appointments per day
    appointments['appointment_date'] = pd.to_datetime(appointments['appointment_date'])
    appointments_per_day = appointments.groupby(
        appointments['appointment_date'].dt.date
    ).size()

    plt.figure()
    appointments_per_day.plot(kind='bar')
    plt.title("Appointments Per Day")
    plt.xlabel("Date")
    plt.ylabel("Number of Appointments")
    plt.tight_layout()
    plt.savefig("appointments_per_day.png")
    plt.close()

    # Revenue by payment status
    revenue_summary = billing.groupby("payment_status")["amount"].sum()

    plt.figure()
    revenue_summary.plot(kind='pie', autopct='%1.1f%%')
    plt.title("Billing Revenue Distribution")
    plt.ylabel("")
    plt.tight_layout()
    plt.savefig("billing_distribution.png")
    plt.close()

    print("Visual reports generated successfully.")

# -----------------------------
# Main Execution
# -----------------------------
def main():
    patients, appointments, billing = fetch_data()
    generate_reports(patients, appointments, billing)
    generate_visualizations(appointments, billing)

    print("Hospital data analysis completed.")

if __name__ == "__main__":
    main()
