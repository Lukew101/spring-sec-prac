"use client"
import { useSearchParams } from "next/navigation";
import Cookies from "js-cookie";
import { useQuery } from '@tanstack/react-query';
import jwtDecode from "jwt-decode"; 
import { useEffect, useState } from "react";

type UserDetails = {
  firstName: string,
  lastName: string,
  email: string,
  picture: string
}

export default function Home() {
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
  }, []);

  const handleLogout = async () => {
    try {
        const response = await fetch("http://localhost:8080/logout", {
            method: "POST",
            credentials: "include", // send cookies with the request
        });
        if (response.ok) {
          console.log("here");
          
        } else {
            console.error("Logout failed");
        }
    } catch (error) {
        console.error("Error during logout: " + error);
    }
};

  if(userDetails) {
    return (
      <div>
      <h2>Personalized Page</h2>
      <a href='http://localhost:8080/login/oauth2/code/google'>Log in with Google</a>
      <button onClick={handleLogout}>Logout</button>    
      {userDetails ? (
        <div>
          <p>First Name: {userDetails.firstName}</p>
          <p>Last Name: {userDetails.lastName}</p>
          <p>Email: {userDetails.email}</p>
          <img src={userDetails.picture}></img>
        </div>
      ) : (
        <p>Loading user details...</p>
      )}
    </div>
    );
  }

  return (
    <a href='http://localhost:8080/login/oauth2/code/google'>Log in with Google</a>
  )

  
}


