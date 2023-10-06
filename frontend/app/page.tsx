"use client"
import { useSearchParams } from "next/navigation";
import Cookies from "js-cookie";
import jwtDecode from "jwt-decode"; 
import { useEffect, useState } from "react";

type UserDetails = {
  firstName: string,
  lastName: string,
  email: string
}

export default function Home() {
  const existingToken = Cookies.get("JwtToken");
  const [userDetails, setUserDetails] = useState<UserDetails>();
  
  useEffect(() => {
    async function fetchData() {
      try {
        const response = await fetch("http://localhost:8080/user", {
          credentials: "include", // send what cookies you have back
        });
  
        if (!response.ok) {
          throw new Error(`HTTP Error! Status: ${response.status}`);
        }
        const data: UserDetails = await response.json();
        console.log(data);
        setUserDetails(data);
      } catch (error) {
        console.error("Error fetching data: " + error);
      }
    }
  
    fetchData(); 
    console.log(userDetails);
  }, []);

  return (
    <div>
    <h2>Personalized Page</h2>
    <a href='http://localhost:8080/login/oauth2/code/google'>Log in with Google</a>
    {userDetails ? (
      <div>
        <p>First Name: {userDetails.firstName}</p>
        <p>Last Name: {userDetails.lastName}</p>
        <p>Email: {userDetails.email}</p>
      </div>
    ) : (
      <p>Loading user details...</p>
    )}
  </div>
  );
}
