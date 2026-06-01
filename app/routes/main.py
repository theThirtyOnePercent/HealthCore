from flask import Blueprint, render_template

@main.route("/")
def home():
    return render_template("index.html")
    
 